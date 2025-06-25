package io.github.livenlearnaday.firebaseauth.usecase.imp

import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import kotlinx.coroutines.flow.Flow

class GetAuthStateUseCaseImp(
    private val authRepository: AuthRepository
) : GetAuthStateUseCase {
    override fun execute(): Flow<FirebaseUser?> = authRepository.getAuthState()
}
