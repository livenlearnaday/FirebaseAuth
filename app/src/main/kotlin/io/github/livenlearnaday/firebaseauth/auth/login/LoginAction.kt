package io.github.livenlearnaday.firebaseauth.auth.login

import android.content.Context

sealed interface LoginAction {
    data object OnSignInAnonymously : LoginAction
    data class OnClickGoogleSignIn(val context: Context) : LoginAction
    data object OnAuthWithEmailAndPassword : LoginAction
    data object OnClickedSignUp : LoginAction
    data object OnResetScreen : LoginAction
}
