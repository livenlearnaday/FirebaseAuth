package io.github.livenlearnaday.firebaseauth.auth.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.data.enum.AuthType
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.usecase.AnonymousSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.FetchCredentialUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetCurrentFirebaseUserUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GoogleSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.LogInWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignUpWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val anonymousSignInUseCase: AnonymousSignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase,
    private val logInWithEmailAndPasswordUseCase: LogInWithEmailAndPasswordUseCase,
    private val fetchCredentialUseCase: FetchCredentialUseCase,
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase
) : ViewModel() {

    companion object {
        @JvmStatic
        private val TAG = LoginViewModel::class.java.simpleName
    }

    private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    var loginState by mutableStateOf(LoginState())
        private set

    private val credentialStateFlow = MutableStateFlow<Credential?>(null)

    init {
        loginState = loginState.copy(
            isLoading = true
        )

        updateAuthState()

        loginState = loginState.copy(
            isLoading = false
        )
    }

    fun onLoginAction(loginAction: LoginAction) {
        when (loginAction) {
            LoginAction.OnSignInAnonymously -> {
                loginState = loginState.copy(
                    authType = AuthType.ANONYMOUS,
                    isLoading = true
                )
                signInAnonymously()
            }

            is LoginAction.OnClickGoogleSignIn -> {
                loginState = loginState.copy(
                    authType = AuthType.GOOGLE,
                    isLoading = true
                )
                fetchCredentials(loginAction.context)
            }

            LoginAction.OnAuthWithEmailAndPassword -> {
                val authRequestModel = AuthRequestModel(
                    email = loginState.email.text.toString().trim(),
                    password = loginState.password.text.toString()
                )
                loginState = loginState.copy(
                    authType = loginState.authType,
                    isLoading = true
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

    private fun updateAuthState() {
        val response = getAuthStateUseCase.execute().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), getCurrentFirebaseUserUseCase.execute())
        loginState = loginState.copy(
            currentUser = response.value,
            firebaseAuthState = response.value?.let { firebaseUser ->
                if (firebaseUser.isAnonymous) FirebaseAuthState.Authenticated else FirebaseAuthState.SignedIn
            } ?: FirebaseAuthState.SignedOut,
            isLoggedIn = response.value != null
        )
    }

    private fun signUpWithEmailAndPassword(authRequestModel: AuthRequestModel) = viewModelScope.launch(defaultExceptionHandler) {
        withContext(coroutineDispatcherProvider.io()) {
            when (
                val response =
                    signUpWithEmailAndPasswordUseCase.execute(authRequestModel)
            ) {
                is Response.Loading -> {
                    Timber.d("$TAG  signUpWithEmailAndPassword Loading")
                    loginState = loginState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> response.data?.let { authResult ->
                    Timber.d("$TAG  signUpWithEmailAndPassword Success: $authResult")

                    loginState = loginState.copy(
                        isLogInSuccess = true,
                        isLoggedIn = true,
                        isLoading = false
                    )
                }

                is Response.Failure -> {
                    Timber.e("$TAG  signUpWithEmailAndPassword Failure ${response.error}")
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

    private fun logInWithEmailAndPassword(authRequestModel: AuthRequestModel) = viewModelScope.launch(defaultExceptionHandler) {
        withContext(coroutineDispatcherProvider.io()) {
            when (val response = logInWithEmailAndPasswordUseCase.execute(authRequestModel)) {
                is Response.Loading -> {
                    Timber.d("$TAG  logInWithEmailAndPassword Loading")
                    loginState = loginState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> response.data?.let { authResult ->
                    Timber.d("$TAG  logInWithEmailAndPassword Success: $authResult")
                    loginState = loginState.copy(
                        isLogInSuccess = true,
                        isLoggedIn = true,
                        isLoading = false
                    )
                }

                is Response.Failure -> {
                    Timber.e("$TAG  logInWithEmailAndPassword Failure ${response.error}")
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
                    Timber.d("$TAG  signInAnonymously Loading")
                    loginState = loginState.copy(
                        isLoading = true
                    )
                }

                is Response.Success -> response.data?.let { authResult ->
                    Timber.d("$TAG  signInAnonymously Success: $authResult")
                    updateAuthState()
                    loginState = loginState.copy(
                        isLogInSuccess = true,
                        isLoggedIn = true,
                        isLoading = false
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

    private fun fetchCredentials(context: Context) {
        viewModelScope.launch(defaultExceptionHandler) {
            withContext(coroutineDispatcherProvider.io()) {
                when (val response = fetchCredentialUseCase.execute(context)) {
                    is Response.Loading -> {
                        loginState = loginState.copy(
                            isLoading = true
                        )
                    }

                    is Response.Success -> response.data?.let { credential ->
                        credentialStateFlow.value = credential
                        signInWithGoogle(credential)
                    }

                    is Response.Failure -> {
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

    private fun signInWithGoogle(credential: Credential) = viewModelScope.launch(defaultExceptionHandler) {
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
}
