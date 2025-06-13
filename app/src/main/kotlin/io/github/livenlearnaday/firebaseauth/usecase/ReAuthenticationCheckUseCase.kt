package io.github.livenlearnaday.firebaseauth.usecase

fun interface ReAuthenticationCheckUseCase {
    suspend fun execute(): Boolean
}
