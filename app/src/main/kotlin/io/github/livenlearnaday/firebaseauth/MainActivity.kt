package io.github.livenlearnaday.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.github.livenlearnaday.firebaseauth.navigation.AppNavigation
import io.github.livenlearnaday.firebaseauth.ui.component.DotPulsingLoadingIndicator
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import org.koin.androidx.compose.koinViewModel

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

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        mainViewModel.mainAction(MainAction.OnUpdateAuthState)
    }

    FirebaseauthTheme {
        if (mainViewModel.mainState.isLoading) {
            DotPulsingLoadingIndicator()
        } else {
            AppNavigation(mainViewModel.mainState.isLoggedIn)
        }
    }
}
