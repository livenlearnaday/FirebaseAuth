package io.github.livenlearnaday.firebaseauth

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.home.HomeScreen
import io.github.livenlearnaday.firebaseauth.home.HomeState
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val rule = createComposeRule()

    private val name = "TestName"
    private val email = "test@email.com"
    private var mockUser: FirebaseUser = mockk<FirebaseUser>()


    @Test
    fun should_show_home_contents() {
        // Arrange
//        mockkStatic(FirebaseAuth::class)
//        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { mockUser.email } returns email
        every { mockUser.displayName } returns name
        every { mockUser.photoUrl } returns "http://random.com".toUri()
        rule.setContent {
            HomeScreen(
                homeState = HomeState(
                    isLoggedIn = true,
                    currentUser = mockUser
                ),
                onHomeAction = { },
                onNavigateToLogIn = { }
            )
        }

        // Act & Assert
        rule.onNodeWithText("Welcome").assertExists()
        rule.onNodeWithText(name).assertExists()
        rule.onNodeWithText(email).assertExists()
        rule.onNodeWithContentDescription("Profile Image").assertExists()

    }

    @Test
    fun should_show_auth_options_when_show_auth_is_true() {
        // Arrange
        rule.setContent {
            HomeScreen(
                homeState = HomeState(
                    isLoggedIn = true,
                    showAuthOptions = true,
                    firebaseAuthState = FirebaseAuthState.SignedIn
                ),
                onHomeAction = { },
                onNavigateToLogIn = { }
            )
        }

        // Act & Assert
        rule.onNodeWithText("Sign-out").assertExists()
        rule.onNodeWithText("Delete Acc").assertExists()
    }
}

