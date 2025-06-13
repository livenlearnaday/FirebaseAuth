@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.livenlearnaday.firebaseauth.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.livenlearnaday.firebaseauth.R
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState
import io.github.livenlearnaday.firebaseauth.ui.component.CustomButton
import io.github.livenlearnaday.firebaseauth.ui.component.CustomImage
import io.github.livenlearnaday.firebaseauth.ui.component.DotPulsingLoadingIndicator
import io.github.livenlearnaday.firebaseauth.ui.theme.FirebaseauthTheme
import io.github.livenlearnaday.firebaseauth.ui.theme.LightGreyishBlue

@Composable
fun HomeScreen(
    homeState: HomeState,
    onHomeAction: (HomeAction) -> Unit,
    onNavigateToLogIn: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(
        key1 = homeState.isLogOutSuccess,
        key2 = homeState.shouldNavigateToLogin,
        key3 = homeState.errorMessage
    ) {
        if (homeState.isLogOutSuccess || homeState.shouldNavigateToLogin) {
            onHomeAction(HomeAction.OnResetScreen)
            onNavigateToLogIn()
        }

        if (!homeState.isLogOutSuccess && homeState.showError) {
            Toast.makeText(
                context,
                homeState.errorMessage.ifEmpty { context.getString(R.string.error_general) },
                Toast.LENGTH_LONG
            ).show()
            onHomeAction(HomeAction.OnResetScreen)
        }
    }

    if (homeState.openDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = {
                onHomeAction(HomeAction.UpdateOpenDeleteAccountDialog(false))
            },
            title = { Text(stringResource(R.string.delete_account)) },
            text = {
                Text(stringResource(R.string.delete_account_warning))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onHomeAction(HomeAction.OnClickedDeleteAccount(context))
                    }
                ) {
                    Text(stringResource(R.string.confirm_delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onHomeAction(HomeAction.UpdateOpenDeleteAccountDialog(false))
                    }
                ) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        )
    }

    Scaffold(
        topBar = { HomeScreenTopUi(homeState, onHomeAction) },
        containerColor = Color.LightGray,
        content = { innerPadding ->

            AnimatedVisibility(homeState.isLoading) {
                DotPulsingLoadingIndicator()
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Card(
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (homeState.currentUser?.email?.isNotEmpty() == true) {
                            Text(
                                text = homeState.currentUser.displayName.toString().ifEmpty {
                                    stringResource(R.string.placeholder_name)
                                },
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = homeState.currentUser.email.toString().ifEmpty {
                                    stringResource(R.string.placeholder_email)
                                }
                            )
                        } else {
                            Text(stringResource(R.string.view_data))
                        }
                    }
                }

                Image(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(300.dp),
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary),
                    alpha = if (homeState.isLoading) 0.2f else 1f
                )

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.text_display_general),
                    style = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = TextUnit.Unspecified
                    )
                )
            }
        }
    )
}

@Composable
fun HomeScreenTopUi(
    homeState: HomeState,
    onHomeAction: (HomeAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            Row(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    stringResource(R.string.welcome),
                    style = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)

                )

                CustomImage(
                    modifier = Modifier
                        .padding(end = 18.dp, bottom = 12.dp)
                        .size(30.dp)
                        .clickable(
                            enabled = !homeState.isLoading,
                            onClick = {
                                onHomeAction(HomeAction.ShowAuthOptions(!homeState.showAuthOptions))
                            }
                        ),
                    imageUrlString = homeState.currentUser?.photoUrl.toString(),
                    placeHolderImageResource = R.drawable.ic_person
                )
            }

            AnimatedVisibility(visible = homeState.showAuthOptions) {
                AuthOptions(
                    homeState = homeState,
                    onHomeAction = onHomeAction
                )
            }
        }
    }
}

@Composable
fun AuthOptions(
    homeState: HomeState,
    onHomeAction: (HomeAction) -> Unit
) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(color = LightGreyishBlue),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomButton(
            onButtonClicked = {
                onHomeAction(HomeAction.OnClickedAuthButton)
            },
            modifier = Modifier
                .size(180.dp, 50.dp)
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            label = if (homeState.firebaseAuthState == FirebaseAuthState.Authenticated) {
                stringResource(
                    R.string.sign_in
                )
            } else {
                stringResource(R.string.sign_out)
            },
            enableButton = !homeState.isLoading

        )

        CustomButton(
            onButtonClicked = {
                // Show message to the user before deleting account
                onHomeAction(HomeAction.UpdateOpenDeleteAccountDialog(true))
            },
            modifier = Modifier
                .size(180.dp, 50.dp)
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            label = stringResource(R.string.delete_acc),
            textColor = Color.Red,
            enableButton = !homeState.isLoading
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen() {
    FirebaseauthTheme {
        HomeScreen(
            homeState = HomeState(
                firebaseAuthState = FirebaseAuthState.SignedIn,
                showAuthOptions = true,
                isLoggedIn = true
            ),
            onHomeAction = {},
            onNavigateToLogIn = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreenLoading() {
    FirebaseauthTheme {
        HomeScreen(
            homeState = HomeState(
                firebaseAuthState = FirebaseAuthState.SignedIn,
                isLoading = true
            ),
            onHomeAction = {},
            onNavigateToLogIn = {}
        )
    }
}
