package io.github.livenlearnaday.firebaseauth.usecase.imp

import androidx.credentials.Credential
import com.google.firebase.auth.AuthResult
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.GoogleSignInUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class GoogleSignInUseCaseImp(
private val authRepository: AuthRepository
): GoogleSignInUseCase {
    override suspend fun execute(credential: Credential): Response<AuthResult> {
        return authRepository.signInWithGoogle(credential)
    }
}