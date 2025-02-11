package io.github.livenlearnaday.firebaseauth.auth.login

import androidx.compose.foundation.text.input.TextFieldState
import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.enum.AuthType
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState

data class LoginState(
    val currentUser: FirebaseUser? = null,
    val firebaseAuthState: FirebaseAuthState = FirebaseAuthState.SignedOut,
    val isLoggedIn: Boolean = false,
    val isLogInSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val authType: AuthType = AuthType.LOGIN,
    val errorMessage: String = "",
    val showError: Boolean = false

)