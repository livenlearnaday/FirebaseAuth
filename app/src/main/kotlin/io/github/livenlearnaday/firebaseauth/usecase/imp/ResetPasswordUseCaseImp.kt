package io.github.livenlearnaday.firebaseauth.usecase.imp

import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.ResetPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class ResetPasswordUseCaseImp(
private val authRepository: AuthRepository
): ResetPasswordUseCase {
    override suspend fun execute(email: String): Response<Boolean> {
        return authRepository.resetPassword(email)
    }
}