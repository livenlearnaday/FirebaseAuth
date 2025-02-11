package io.github.livenlearnaday.firebaseauth.usecase.imp

import androidx.credentials.Credential
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class DeleteUserAccountUseCaseImp(
private val authRepository: AuthRepository
): DeleteUserAccountUseCase {
    override suspend fun execute(credential: Credential?): Response<Boolean> {
        return authRepository.deleteUserAccount(credential)
    }
}