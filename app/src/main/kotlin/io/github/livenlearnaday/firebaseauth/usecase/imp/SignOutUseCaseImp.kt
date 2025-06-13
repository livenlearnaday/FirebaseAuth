package io.github.livenlearnaday.firebaseauth.usecase.imp

import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.SignOutUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class SignOutUseCaseImp(
    private val authRepository: AuthRepository
) : SignOutUseCase {
    override suspend fun execute(): Response<Boolean> = authRepository.signOut()
}
