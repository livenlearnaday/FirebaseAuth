package io.github.livenlearnaday.firebaseauth.usecase

import io.github.livenlearnaday.firebaseauth.util.Response

fun interface SignOutUseCase {
    suspend fun execute(): Response<Boolean>
}