package io.github.livenlearnaday.firebaseauth.usecase

import io.github.livenlearnaday.firebaseauth.util.Response

fun interface ResetPasswordUseCase {
    suspend fun execute(email: String): Response<Boolean>
}