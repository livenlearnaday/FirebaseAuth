@file:OptIn(ExperimentalFoundationApi::class)

package io.github.livenlearnaday.firebaseauth.auth.reset

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.livenlearnaday.firebaseauth.R
import io.github.livenlearnaday.firebaseauth.ui.component.CustomButton
import io.github.livenlearnaday.firebaseauth.ui.component.EmailTextField
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    resetPasswordState: ResetPasswordState,
    modifier: Modifier = Modifier,
    onResetPasswordAction: (ResetPasswordAction) -> Unit,
    onBackPressed: () -> Unit,
    onResetPasswordSuccess: (message: String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = resetPasswordState.isResetPasswordSuccess) {
        if (resetPasswordState.isResetPasswordSuccess && resetPasswordState.message.isNotBlank()) {
            onResetPasswordSuccess(resetPasswordState.message)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onBackPressed()
                        },
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                        contentDescription = "Go Back"
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                modifier = Modifier.fillMaxWidth(),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_security),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.reset_password_title),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            EmailTextField(
                state = resetPasswordState.email,
                hint = stringResource(R.string.email)
            )
            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                label = stringResource(R.string.reset),
                onButtonClicked = {
                    keyboard?.hide()
                    onResetPasswordAction(ResetPasswordAction.OnPasswordResetClicked)
                },
                showLoading = resetPasswordState.isLoading
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
fun PreviewResetScreen() {
    FirebaseauthTheme {
        ResetPasswordScreen(
            resetPasswordState = ResetPasswordState(),
            onResetPasswordAction = {},
            onBackPressed = {},
            onResetPasswordSuccess = {
            }
        )
    }
}
