package io.github.livenlearnaday.firebaseauth.home

import android.content.Context

sealed interface HomeAction {
    data object OnSignOut : HomeAction
    data class UpdateOpenDeleteAccountDialog(val shouldOpen: Boolean): HomeAction
    data object OnClickedAuthButton: HomeAction
    data class OnClickedDeleteAccount(val context: Context): HomeAction
    data object OnResetScreen: HomeAction
    data class ShowAuthOptions(val shouldShow: Boolean): HomeAction
}