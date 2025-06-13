package io.github.livenlearnaday.firebaseauth.usecase.imp

import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.ReAuthenticationCheckUseCase

class ReAuthenticationCheckUseCaseImp(
    private val authRepository: AuthRepository
) : ReAuthenticationCheckUseCase {
    override suspend fun execute(): Boolean = authRepository.checkNeedsReAuth()
}
