@file:OptIn(ExperimentalFoundationApi::class)

package io.github.livenlearnaday.firebaseauth.auth.reset

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState

data class ResetPasswordState(
    val email: TextFieldState = TextFieldState(),
    val isResetPasswordSuccess: Boolean = false,
    val message: String = "",
    val isLoading: Boolean = false
)
