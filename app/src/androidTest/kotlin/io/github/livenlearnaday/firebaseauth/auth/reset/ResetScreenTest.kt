package io.github.livenlearnaday.firebaseauth.auth.reset

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class ResetScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun should_enter_email_for_reset_password() {
        rule.setContent {
            ResetPasswordScreen(
                resetPasswordState = ResetPasswordState(),
                onResetPasswordAction = { },
                onBackPressed = { },
                onResetPasswordSuccess = { }
            )
        }

        val email = "test@email.com"

        rule.onNodeWithText("Email").assertExists()
        rule.onNodeWithTag("email").performTextInput(email)
        rule.onNodeWithText(email).isDisplayed()
        rule.onNodeWithText("Reset").performClick()
    }
}
