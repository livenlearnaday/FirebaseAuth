package io.github.livenlearnaday.firebaseauth.usecase.imp

import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.SignUpWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class SignUpWithEmailAndPasswordUseCaseImp     (
    private val authRepository: AuthRepository
): SignUpWithEmailAndPasswordUseCase {
    override suspend fun execute(authRequestModel: AuthRequestModel): Response<Boolean> {
        return authRepository.signUpWithEmailAndPassword(authRequestModel)
    }

}