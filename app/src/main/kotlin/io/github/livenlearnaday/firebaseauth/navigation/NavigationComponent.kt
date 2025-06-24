package io.github.livenlearnaday.firebaseauth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.livenlearnaday.firebaseauth.auth.login.LoginScreen
import io.github.livenlearnaday.firebaseauth.auth.login.LoginViewModel
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordScreen
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordViewModel
import io.github.livenlearnaday.firebaseauth.home.HomeScreen
import io.github.livenlearnaday.firebaseauth.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) NavigationRoute.HomeRoute else NavigationRoute.LoginRoute
    ) {
        composable<NavigationRoute.LoginRoute> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            val loginState = loginViewModel.loginState
            LoginScreen(
                loginState = loginState,
                onLoginAction = loginViewModel::onLoginAction,
                onLogInSuccess = {
                    navController.navigate(NavigationRoute.HomeRoute)
                },
                onForgotPassword = {
                    navController.navigate(NavigationRoute.ResetPasswordRoute)
                }
            )
        }
        composable<NavigationRoute.HomeRoute> {
            val homeViewModel = koinViewModel<HomeViewModel>()
            val homeState = homeViewModel.homeState
            HomeScreen(
                homeState = homeState,
                onHomeAction = homeViewModel::onHomeAction,
                onNavigateToLogIn = {
                    navController.navigate(NavigationRoute.LoginRoute) {
                        popUpTo(NavigationRoute.LoginRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<NavigationRoute.ResetPasswordRoute> {
            val resetPasswordViewModel = koinViewModel<ResetPasswordViewModel>()
            val resetPasswordState = resetPasswordViewModel.resetPasswordState
            ResetPasswordScreen(
                resetPasswordState = resetPasswordState,
                onResetPasswordAction = resetPasswordViewModel::resetPasswordAction,
                onBackPressed = {
                    navController.popBackStack()
                },
                onResetPasswordSuccess = {
                    navController.navigate(NavigationRoute.LoginRoute) {
                        popUpTo(NavigationRoute.LoginRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
