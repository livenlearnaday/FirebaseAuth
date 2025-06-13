package io.github.livenlearnaday.firebaseauth.usecase.imp

import com.google.firebase.auth.AuthResult
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.AnonymousSignInUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class AnonymousSignInUseCaseImp(
    private val authRepository: AuthRepository
) : AnonymousSignInUseCase {
    override suspend fun execute(): Response<AuthResult> = authRepository.signInAnonymously()
}
