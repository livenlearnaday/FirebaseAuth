package io.github.livenlearnaday.firebaseauth.usecase.imp

import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.GetCurrentFirebaseUserUseCase

class GetCurrentFirebaseUserUseCaseImp(
    private val authRepository: AuthRepository
) : GetCurrentFirebaseUserUseCase {
    override fun execute(): FirebaseUser? = authRepository.currentUser
}
