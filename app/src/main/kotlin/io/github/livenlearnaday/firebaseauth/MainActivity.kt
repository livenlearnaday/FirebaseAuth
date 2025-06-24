package io.github.livenlearnaday.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.livenlearnaday.firebaseauth.navigation.AppNavigation
import io.github.livenlearnaday.firebaseauth.ui.component.DotPulsingLoadingIndicator
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val mainViewModel: MainViewModel = koinViewModel()
    FirebaseauthTheme {
        if (mainViewModel.mainState.isLoading) {
            DotPulsingLoadingIndicator()
        } else {
            AppNavigation(mainViewModel.mainState.isLoggedIn)
        }
    }
}
