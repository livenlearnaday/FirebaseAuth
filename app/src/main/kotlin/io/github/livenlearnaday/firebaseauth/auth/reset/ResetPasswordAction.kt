package io.github.livenlearnaday.firebaseauth.auth.reset

sealed interface ResetPasswordAction {
    data object OnPasswordResetClicked : ResetPasswordAction
}
