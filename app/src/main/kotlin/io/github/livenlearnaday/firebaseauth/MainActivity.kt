package io.github.livenlearnaday.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.livenlearnaday.firebaseauth.auth.AuthViewModel
import io.github.livenlearnaday.firebaseauth.navigation.HomeScreenRoute
import io.github.livenlearnaday.firebaseauth.navigation.LoginScreenRoute
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModel<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                authViewModel.mainState.isLoading
            }
        }

        setContent {
            KoinContext {
                FirebaseauthTheme {
                    if (authViewModel.mainState.isLoggedIn) {
                        Navigator(screen = HomeScreenRoute)
                    } else {
                        Navigator(screen = LoginScreenRoute)
                    }
                }
            }
        }
    }
}
