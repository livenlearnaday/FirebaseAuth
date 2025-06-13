package io.github.livenlearnaday.firebaseauth.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.livenlearnaday.firebaseauth.R
import io.github.livenlearnaday.firebaseauth.data.enum.AuthType
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.ui.component.ActionText
import io.github.livenlearnaday.firebaseauth.ui.component.CustomButton
import io.github.livenlearnaday.firebaseauth.ui.component.EmailTextField
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import io.github.livenlearnaday.presentation.components.PasswordTextField

@Composable
fun LoginScreen(
    loginState: LoginState,
    onLoginAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    onLogInSuccess: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(loginState.showError) {
        if (loginState.showError) {
            keyboard?.hide()
            Toast.makeText(context, loginState.errorMessage.ifEmpty { context.getString(R.string.error_general) }, Toast.LENGTH_LONG).show()
            onLoginAction(LoginAction.OnResetScreen)
        }
    }

    LaunchedEffect(loginState.isLogInSuccess) {
        if (loginState.isLogInSuccess) {
            keyboard?.hide()
            onLoginAction(LoginAction.OnResetScreen)
            onLogInSuccess()
        }
    }

    /*val launcherSignIn =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onLoginAction(LoginAction.OnSignInWithGoogle(result.data))
            } else {
                if (result.resultCode == Activity.RESULT_CANCELED) {
                    Timber.e("LoginScreen, launcherSignIn OneTapSignInWithGoogle Cancelled")
                }
                onLoginAction(LoginAction.OnResetScreen)
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcherSignIn.launch(intent)
    }*/

/*
    LaunchedEffect(loginState.beginSignInResult) {
        if (loginState.beginSignInResult != null) {
            launch(loginState.beginSignInResult)
        }
    }
*/

    Scaffold(
        containerColor = Color.LightGray,
        content = { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .wrapContentSize(Alignment.TopCenter)
                    .padding(innerPadding),
                contentAlignment = Alignment.TopStart

            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ConstraintLayout {
                        val (
                            imageLogin,
                            inputLayout
                        ) = createRefs()

                        Image(
                            modifier = Modifier
                                .size(200.dp)
                                .constrainAs(imageLogin) {
                                    top.linkTo(parent.top, margin = 30.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.ic_security),
                            contentDescription = null,
                            contentScale = ContentScale.Fit

                        )

                        Column(
                            modifier = Modifier
                                .constrainAs(inputLayout) {
                                    top.linkTo(imageLogin.bottom, margin = 30.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EmailTextField(
                                modifier = Modifier
                                    .size(width = 400.dp, height = 50.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                state = loginState.email,
                                hint = stringResource(R.string.email)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            PasswordTextField(
                                modifier = Modifier
                                    .size(width = 400.dp, height = 50.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                state = loginState.password,
                                hint = stringResource(R.string.password)
                            )

                            Spacer(modifier = Modifier.height(36.dp))

                            CustomButton(
                                onButtonClicked = {
                                    keyboard?.hide()
                                    onLoginAction(LoginAction.OnAuthWithEmailAndPassword)
                                },
                                modifier = Modifier
                                    .size(width = 300.dp, height = 50.dp)
                                    .padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                label = if (loginState.authType == AuthType.SIGNUP) {
                                    stringResource(R.string.sign_up)
                                } else {
                                    stringResource(
                                        R.string.login
                                    )
                                },
                                enableButton = !loginState.isLoading,
                                showLoading = loginState.isLoading && (loginState.authType == AuthType.LOGIN || loginState.authType == AuthType.SIGNUP)
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row {
                                if (loginState.authType != AuthType.SIGNUP) {
                                    ActionText(
                                        onActionTextClick = {
                                            onForgotPassword()
                                        },
                                        resourceId = R.string.forgot_password,
                                        enableTextClick = !loginState.isLoading
                                    )
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 4.dp,
                                            end = 4.dp
                                        ),
                                        text = stringResource(R.string.vertical_divider),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                ActionText(
                                    onActionTextClick = {
                                        onLoginAction(LoginAction.OnClickedSignUp)
                                    },
                                    resourceId = if (loginState.authType == AuthType.SIGNUP) R.string.login else R.string.sign_up,
                                    enableTextClick = !loginState.isLoading
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            CustomButton(
                                onButtonClicked = {
                                    onLoginAction(LoginAction.OnClickGoogleSignIn(context))
                                },
                                modifier = Modifier
                                    .size(width = 300.dp, height = 50.dp)
                                    .padding(horizontal = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                label = stringResource(R.string.sign_in_with_google),
                                imageResource = R.drawable.ic_google_logo,
                                enableButton = !loginState.isLoading,
                                showLoading = loginState.isLoading && loginState.authType == AuthType.GOOGLE
                            )

                            Spacer(Modifier.height(16.dp))

                            if (loginState.firebaseAuthState == FirebaseAuthState.SignedOut) {
                                CustomButton(
                                    onButtonClicked = {
                                        onLoginAction(LoginAction.OnSignInAnonymously)
                                    },
                                    modifier = Modifier
                                        .size(width = 300.dp, height = 50.dp)
                                        .padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White
                                    ),
                                    label = stringResource(R.string.skip),
                                    enableButton = !loginState.isLoading,
                                    showLoading = loginState.isLoading && loginState.authType == AuthType.ANONYMOUS
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen() {
    FirebaseauthTheme {
        LoginScreen(
            loginState = LoginState(),
            onLoginAction = {},
            onLogInSuccess = {},
            onForgotPassword = {}
        )
    }
}
