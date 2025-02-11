package io.github.livenlearnaday.firebaseauth.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordScreen
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordViewModel
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import org.koin.androidx.compose.koinViewModel

object ResetScreenRoute : Screen {
    private fun readResolve(): Any = ResetScreenRoute

    @Composable
    override fun Content() {
        val resetPasswordViewModel = koinViewModel<ResetPasswordViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        FirebaseauthTheme {
            ResetPasswordScreen(
                resetPasswordState = resetPasswordViewModel.resetPasswordState,
                onBackPressed = {
                    navigator.pop()
                },
                onResetPasswordSuccess = {
                    navigator.replace(LoginScreenRoute)
                },
                onResetPasswordAction = resetPasswordViewModel::resetPasswordAction
            )
        }
    }
}