package io.github.livenlearnaday.firebaseauth.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.MainState
import io.github.livenlearnaday.firebaseauth.auth.login.LoginAction
import io.github.livenlearnaday.firebaseauth.auth.login.LoginState
import io.github.livenlearnaday.firebaseauth.data.enum.AuthType
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.home.HomeAction
import io.github.livenlearnaday.firebaseauth.home.HomeState
import io.github.livenlearnaday.firebaseauth.usecase.AnonymousSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.usecase.FetchCredentialUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GoogleSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.LogInWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.usecase.ReAuthenticationCheckUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignOutUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignUpWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AuthViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val anonymousSignInUseCase: AnonymousSignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val reAuthenticationCheckUseCase: ReAuthenticationCheckUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase,
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase,
    private val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
    private val fetchCredentialUseCase: FetchCredentialUseCase
) : ViewModel() {

    companion object {
        @JvmStatic
        private val TAG = AuthViewModel::class.java.simpleName
    }

    private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    var mainState by mutableStateOf(MainState())
        private set

    var loginState by mutableStateOf(LoginState())
        private set

    var homeState by mutableStateOf(HomeState())
        private set


    private val credentialStateFlow = MutableStateFlow<Credential?>(null)

    init {
        mainState = mainState.copy(
            isLoading = true
        )

        updateAuthState()


        mainState = mainState.copy(
            isLoading = false
        )

    }

    private fun updateAuthState() {

        val response = getAuthStateUseCase.execute(viewModelScope)
        mainState = mainState.copy(
            currentUser = response.value,
            isLoggedIn = response.value != null
        )
        loginState = loginState.copy(
            currentUser = response.value,
            firebaseAuthState = response.value?.let { firebaseUser ->
                if (firebaseUser.isAnonymous) FirebaseAuthState.Authenticated else FirebaseAuthState.SignedIn
            } ?: FirebaseAuthState.SignedOut,
            isLoggedIn = response.value != null
        )
        homeState = homeState.copy(
            currentUser = response.value,
            firebaseAuthState = response.value?.let { firebaseUser ->
                if (firebaseUser.isAnonymous) FirebaseAuthState.Authenticated else FirebaseAuthState.SignedIn
            } ?: FirebaseAuthState.SignedOut,
            isLoggedIn = response.value != null
        )

    }


    private fun fetchCredentials(context: Context) {
        viewModelScope.launch(defaultExceptionHandler) {
            loginState = loginState.copy(
                isLoading = true
            )
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = fetchCredentialUseCase.execute(context)) {
                    is Response.Loading -> {
                        loginState = loginState.copy(
                            isLoading = true
                        )
                        Timber.d("$TAG fetchCredentials Loading")
                    }

                    is Response.Success -> response.data?.let { credential ->
                        Timber.d("$TAG fetchCredentials Success getCredentialResponse.credential: ${credential.type}")
                        Timber.d("$TAG fetchCredentials Success getCredentialResponse.credential.data: ${credential.data}")

                        credentialStateFlow.value = credential
                        if (!homeState.isDeletingAccount) {
                            signInWithGoogle(credential)
                        }

                    }

                    is Response.Failure -> {
                        Timber.e("$TAG fetchCredentials Failure ${response.error}")
                        loginState = loginState.copy(
                            isLoading = false,
                            isLogInSuccess = false,
                            errorMessage = response.error.message ?: "",
                            showError = true
                        )
                    }

                }
            }
        }
    }

    fun onLoginAction(loginAction: LoginAction) {
        when (loginAction) {
            LoginAction.OnSignInAnonymously -> {
                loginState = loginState.copy(
                    authType = AuthType.ANONYMOUS
                )
                signInAnonymously()
            }

            is LoginAction.OnClickGoogleSignIn -> {
                loginState = loginState.copy(
                    authType = AuthType.GOOGLE
                )
                fetchCredentials(loginAction.context)
            }

            LoginAction.OnAuthWithEmailAndPassword -> {
                val authRequestModel = AuthRequestModel(
                    email = loginState.email.text.toString().trim(),
                    password = loginState.password.text.toString()
                )
                when (loginState.authType) {
                    AuthType.SIGNUP -> {
                        signUpWithEmailAndPassword(authRequestModel)
                    }

                    AuthType.LOGIN -> {
                        logInWithEmailAndPassword(authRequestModel)
                    }

                    else -> {} // nop
                }
            }

            LoginAction.OnClickedSignUp -> {
                loginState =
                    loginState.copy(
                        authType = if (loginState.authType == AuthType.LOGIN) AuthType.SIGNUP else AuthType.LOGIN
                    )
            }

            LoginAction.OnResetScreen -> {
                loginState = loginState.copy(
                    isLogInSuccess = false,
                    errorMessage = "",
                    isLoading = false,
                    showError = false
                )
            }
        }
    }


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


    private fun signUpWithEmailAndPassword(authRequestModel: AuthRequestModel) =
        viewModelScope.launch(defaultExceptionHandler) {
            loginState = loginState.copy(
                isLoading = true
            )
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = signUpWithEmailAndPasswordUseCase.execute(authRequestModel)) {
                    is Response.Loading -> {
                        Timber.d("$TAG signUpWithEmailAndPassword Loading")
                        loginState = loginState.copy(
                            isLoading = true
                        )
                    }

                    is Response.Success -> response.data?.let { authResult ->
                        Timber.d("$TAG signUpWithEmailAndPassword Success: $authResult")
                        homeState = homeState.copy(
                            isLoggedIn = true
                        )

                        loginState = loginState.copy(
                            isLogInSuccess = true,
                            isLoggedIn = true
                        )
                        mainState = mainState.copy(
                            isLoggedIn = true
                        )

                    }

                    is Response.Failure -> {
                        Timber.e("$TAG signUpWithEmailAndPassword Failure ${response.error}")
                        loginState = loginState.copy(
                            isLoading = false,
                            isLogInSuccess = false,
                            errorMessage = response.error.message ?: "",
                            showError = true

                        )
                    }

                }
            }
        }

    private fun logInWithEmailAndPassword(authRequestModel: AuthRequestModel) =
        viewModelScope.launch(defaultExceptionHandler) {
            loginState = loginState.copy(
                isLoading = true
            )
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = logInWithEmailAndPasswordUseCase.execute(authRequestModel)) {
                    is Response.Loading -> {
                        Timber.d("$TAG logInWithEmailAndPassword Loading")
                        loginState = loginState.copy(
                            isLoading = true
                        )
                    }

                    is Response.Success -> response.data?.let { authResult ->
                        Timber.d("$TAG logInWithEmailAndPassword Success: $authResult")
                        loginState = loginState.copy(
                            isLogInSuccess = true,
                            isLoggedIn = true
                        )
                        homeState = homeState.copy(
                            isLoggedIn = true
                        )
                        mainState = mainState.copy(
                            isLoggedIn = true
                        )

                    }

                    is Response.Failure -> {
                        Timber.e("$TAG logInWithEmailAndPassword Failure ${response.error}")
                        loginState = loginState.copy(
                            isLoading = false,
                            isLogInSuccess = false,
                            errorMessage = response.error.message ?: "",
                            showError = true
                        )
                    }

                }
            }
        }


    private fun signInAnonymously() = viewModelScope.launch(defaultExceptionHandler) {
        withContext(coroutineDispatcherProvider.io()) {
            when (val response = anonymousSignInUseCase.execute()) {
                is Response.Loading -> {
                    Timber.d("$TAG signInAnonymously Loading")
                    loginState = loginState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> response.data?.let { authResult ->
                    Timber.d("$TAG signInAnonymously Success: $authResult")
                    updateAuthState()
                    loginState = loginState.copy(
                        isLogInSuccess = true,
                        isLoggedIn = true
                    )
                    homeState = homeState.copy(
                        isLoggedIn = true
                    )
                    mainState = mainState.copy(
                        isLoggedIn = true
                    )

                }

                is Response.Failure -> {
                    Timber.e("$TAG signInAnonymously Failure ${response.error}")
                    updateAuthState()
                    loginState = loginState.copy(
                        isLoading = false,
                        isLogInSuccess = false,
                        errorMessage = response.error.message ?: "",
                        showError = true
                    )

                }

            }
        }
    }

    private fun signInWithGoogle(credential: Credential) =
        viewModelScope.launch(defaultExceptionHandler) {
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = googleSignInUseCase.execute(credential)) {
                    is Response.Loading -> {
                        Timber.d("$TAG signInWithGoogle Loading")
                        loginState = loginState.copy(
                            isLoading = true
                        )
                    }

                    is Response.Success -> {
                        response.data?.let { authResult ->
                            Timber.d("$TAG signInWithGoogle Success: $authResult")
                            updateAuthState()
                            loginState = loginState.copy(
                                isLogInSuccess = true,
                                isLoggedIn = true,
                                isLoading = false
                            )
                            homeState = homeState.copy(
                                isLoggedIn = true
                            )
                            mainState = mainState.copy(
                                isLoggedIn = true
                            )
                        }
                    }

                    is Response.Failure -> {
                        Timber.e("$TAG signInWithGoogle Failure ${response.error}")
                        updateAuthState()
                        loginState = loginState.copy(
                            isLoading = false,
                            isLogInSuccess = false,
                            errorMessage = response.error.message ?: "",
                            showError = true
                        )

                    }

                }
            }
        }

    private fun signOut() = viewModelScope.launch(defaultExceptionHandler) {
        homeState = homeState.copy(
            isLoading = true
        )

        withContext(coroutineDispatcherProvider.io()) {
            when (val response = signOutUseCase.execute()) {
                is Response.Loading -> {
                    Timber.d("$TAG signOut isLoading")
                    homeState = homeState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> {
                    response.data?.let { signOutResult ->
                        Timber.d("$TAG signOut Success: $signOutResult")
                        updateAuthState()
                        homeState = homeState.copy(
                            isLogOutSuccess = true,
                            isLoggedIn = false
                        )
                        mainState = mainState.copy(
                            isLoggedIn = false
                        )
                        loginState = loginState.copy(
                            isLoggedIn = false,
                            authType = AuthType.LOGIN
                        )
                    }
                }

                is Response.Failure -> {
                    Timber.e("$TAG signOut Failure ${response.error}")
                    updateAuthState()
                    homeState = homeState.copy(
                        isLoading = false,
                        isLogOutSuccess = false,
                        errorMessage = response.error.message ?: "",
                        showError = true
                    )
                    loginState = loginState.copy(
                        authType = AuthType.LOGIN
                    )

                }
            }
        }
    }

    private fun deleteAccount(credential: Credential?) =
        viewModelScope.launch(defaultExceptionHandler) {
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = deleteUserAccountUseCase.execute(credential)) {
                    is Response.Loading -> {
                        Timber.d("$TAG deleteAccount Loading")
                        homeState = homeState.copy(
                            isLoading = true
                        )

                    }

                    is Response.Success -> {
                        response.data?.let { authResult ->
                            Timber.d("$TAG deleteAccount Success: $authResult")
                            updateAuthState()
                            homeState = homeState.copy(
                                isLogOutSuccess = true,
                                isLoggedIn = false,
                                isDeletingAccount = false
                            )
                            mainState = mainState.copy(
                                isLoggedIn = false
                            )
                            loginState = loginState.copy(
                                isLoggedIn = false,
                                authType = AuthType.LOGIN
                            )
                        }
                    }

                    is Response.Failure -> {
                        Timber.e("$TAG deleteAccount Failure ${response.error}")
                        updateAuthState()
                        homeState = homeState.copy(
                            isLoading = false,
                            isLogOutSuccess = false,
                            errorMessage = response.error.message ?: "",
                            showError = true,
                            isDeletingAccount = false
                        )
                        loginState = loginState.copy(
                            authType = AuthType.LOGIN
                        )

                    }
                }
            }
        }

    private fun checkNeedsReAuth(context: Context) =
        viewModelScope.launch(defaultExceptionHandler) {
            homeState = homeState.copy(
                isLoading = true,
                openDeleteAccountDialog = false,
                isDeletingAccount = true
            )
            withContext(coroutineDispatcherProvider.io()) {
                when (reAuthenticationCheckUseCase.execute()) {
                    true -> {
                        viewModelScope.launch(defaultExceptionHandler) {
                            withContext(coroutineDispatcherProvider.io()) {
                                fetchCredentials(context)

                                credentialStateFlow.value?.let { credential ->
                                    deleteAccount(credential)
                                } ?: deleteAccount(null)

                            }
                        }
                    }

                    else -> deleteAccount(null)

                }

            }
        }
}