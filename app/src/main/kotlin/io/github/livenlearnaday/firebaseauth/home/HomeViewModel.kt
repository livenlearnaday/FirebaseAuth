package io.github.livenlearnaday.firebaseauth.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.MainViewModel
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.ReAuthenticationCheckUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignOutUseCase
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val reAuthenticationCheckUseCase: ReAuthenticationCheckUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase
) : ViewModel() {

    companion object {
        @JvmStatic
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    var homeState by mutableStateOf(HomeState())
        private set

    fun onHomeAction(homeAction: HomeAction) {
        when (homeAction) {
            HomeAction.OnSignOut -> signOut()

            is HomeAction.UpdateOpenDeleteAccountDialog -> {
                homeState = homeState.copy(
                    openDeleteAccountDialog = homeAction.shouldOpen
                )
            }

            HomeAction.OnClickedAuthButton -> {
                when (homeState.firebaseAuthState) {
                    FirebaseAuthState.Authenticated -> {
                        homeState = homeState.copy(
                            shouldNavigateToLogin = true,
                            isLoading = true
                        )
                    }

                    else -> signOut()
                }
            }

            is HomeAction.OnClickedDeleteAccount -> {
                checkNeedsReAuth(homeAction.context)
            }

            HomeAction.OnResetScreen -> {
                homeState = homeState.copy(
                    isLogOutSuccess = false,
                    errorMessage = "",
                    isLoading = false,
                    shouldNavigateToLogin = false,
                    openDeleteAccountDialog = false,
                    showError = false,
                    showAuthOptions = false,
                    isDeletingAccount = false
                )
            }

            is HomeAction.ShowAuthOptions -> {
                homeState = homeState.copy(
                    showAuthOptions = homeAction.shouldShow
                )
            }
        }
    }

    private fun updateAuthState() {
        val response = getAuthStateUseCase.execute(viewModelScope)

        homeState = homeState.copy(
            currentUser = response.value,
            firebaseAuthState = response.value?.let { firebaseUser ->
                if (firebaseUser.isAnonymous) FirebaseAuthState.Authenticated else FirebaseAuthState.SignedIn
            } ?: FirebaseAuthState.SignedOut,
            isLoggedIn = response.value != null
        )
    }

    private fun signOut() = viewModelScope.launch(defaultExceptionHandler) {
        homeState = homeState.copy(
            isLoading = true
        )

        withContext(coroutineDispatcherProvider.io()) {
            when (val response = signOutUseCase.execute()) {
                is Response.Loading -> {
                    Timber.d("$TAG  signOut isLoading")
                    homeState = homeState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> {
                    response.data?.let { signOutResult ->
                        Timber.d("$TAG  signOut Success: $signOutResult")
                        updateAuthState()
                        homeState = homeState.copy(
                            isLogOutSuccess = true,
                            isLoggedIn = false
                        )
                    }
                }

                is Response.Failure -> {
                    Timber.e("$TAG  signOut Failure ${response.error}")
                    updateAuthState()
                    homeState = homeState.copy(
                        isLoading = false,
                        isLogOutSuccess = false,
                        errorMessage = response.error.message ?: "",
                        showError = true
                    )
                }
            }
        }
    }

    private fun deleteAccount(context: Context, needReAuth: Boolean) = viewModelScope.launch(defaultExceptionHandler) {
        withContext(coroutineDispatcherProvider.io()) {
            when (val response = deleteUserAccountUseCase.execute(context, needReAuth)) {
                is Response.Loading -> {
                    Timber.d("$TAG  deleteAccount Loading")
                    homeState = homeState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> {
                    response.data?.let { authResult ->
                        Timber.d("$TAG  deleteAccount Success: $authResult")
                        updateAuthState()
                        homeState = homeState.copy(
                            isLogOutSuccess = true,
                            isLoggedIn = false,
                            isDeletingAccount = false
                        )
                    }
                }

                is Response.Failure -> {
                    Timber.e("$TAG  deleteAccount Failure ${response.error}")
                    updateAuthState()
                    homeState = homeState.copy(
                        isLoading = false,
                        isLogOutSuccess = false,
                        errorMessage = response.error.message ?: "",
                        showError = true,
                        isDeletingAccount = false
                    )
                }
            }
        }
    }

    private fun checkNeedsReAuth(context: Context) = viewModelScope.launch(defaultExceptionHandler) {
        homeState = homeState.copy(
            isLoading = true,
            openDeleteAccountDialog = false,
            isDeletingAccount = true
        )
        withContext(coroutineDispatcherProvider.io()) {
            when (reAuthenticationCheckUseCase.execute()) {
                true -> deleteAccount(context, true)
                else -> deleteAccount(context, false)
            }
        }
    }
}
