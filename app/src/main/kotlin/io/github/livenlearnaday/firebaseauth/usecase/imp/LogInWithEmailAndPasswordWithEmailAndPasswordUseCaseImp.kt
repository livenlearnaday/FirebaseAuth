package io.github.livenlearnaday.firebaseauth.usecase.imp

import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.LogInWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class LogInWithEmailAndPasswordUseCaseImp     (
    private val authRepository: AuthRepository
): LogInWithEmailAndPasswordUseCase {
    override suspend fun execute(authRequestModel: AuthRequestModel): Response<Boolean> {
        return authRepository.loginWithEmailAndPassword(authRequestModel)
    }

}