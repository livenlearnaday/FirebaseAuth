package io.github.livenlearnaday.firebaseauth.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.livenlearnaday.firebaseauth.auth.AuthViewModel
import io.github.livenlearnaday.firebaseauth.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

data object HomeScreenRoute : Screen {
    private fun readResolve(): Any = HomeScreenRoute

    @Composable
    override fun Content() {
        val authViewModel = koinViewModel<AuthViewModel>()
        val homeState = authViewModel.homeState
        val navigator = LocalNavigator.currentOrThrow

        HomeScreen(
            homeState = homeState,
            onHomeAction = authViewModel::onHomeAction,
            onNavigateToLogIn = {
                navigator.replace(LoginScreenRoute)
            }
        )
    }
}
