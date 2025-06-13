package io.github.livenlearnaday.firebaseauth.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.livenlearnaday.firebaseauth.auth.AuthViewModel
import io.github.livenlearnaday.firebaseauth.auth.login.LoginScreen
import org.koin.androidx.compose.koinViewModel

data object LoginScreenRoute : Screen {
    private fun readResolve(): Any = LoginScreenRoute

    @Composable
    override fun Content() {
        val authViewModel = koinViewModel<AuthViewModel>()
        val loginState = authViewModel.loginState
        val navigator = LocalNavigator.currentOrThrow

        LoginScreen(
            loginState = loginState,
            onLoginAction = authViewModel::onLoginAction,
            onLogInSuccess = {
                navigator.replace(HomeScreenRoute)
            },
            onForgotPassword = {
                navigator.push(ResetScreenRoute)
            }
        )
    }
}
