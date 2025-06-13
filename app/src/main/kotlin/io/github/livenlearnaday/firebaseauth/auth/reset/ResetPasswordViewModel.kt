@file:OptIn(ExperimentalFoundationApi::class)

package io.github.livenlearnaday.firebaseauth.auth.reset

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.usecase.ResetPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    var resetPasswordState by mutableStateOf(ResetPasswordState())
        private set

    fun resetPasswordAction(resetPasswordAction: ResetPasswordAction) {
        when (resetPasswordAction) {
            ResetPasswordAction.OnPasswordResetClicked -> {
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            resetPasswordState = resetPasswordState.copy(
                isLoading = true
            )

            /* Make request to reset password */
            val resetPasswordResult = resetPasswordUseCase
                .execute(resetPasswordState.email.text.toString().trim())

            when (resetPasswordResult) {
                is Response.Success -> {
                    resetPasswordState = resetPasswordState.copy(
                        isResetPasswordSuccess = true,
                        message = "Reset successful, Plese check you email for further instruction."
                    )
                }

                is Response.Failure -> {
                    resetPasswordState = resetPasswordState.copy(
                        isResetPasswordSuccess = false
                    )
                }

                Response.Loading -> {
                    resetPasswordState = resetPasswordState.copy(
                        isLoading = true
                    )
                }
            }

            resetPasswordState = resetPasswordState.copy(
                isLoading = false
            )
        }
    }
}
