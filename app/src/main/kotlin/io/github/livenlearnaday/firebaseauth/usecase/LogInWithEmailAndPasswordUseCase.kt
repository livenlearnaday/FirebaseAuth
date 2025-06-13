package io.github.livenlearnaday.firebaseauth.usecase

import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.util.Response

interface LogInWithEmailAndPasswordUseCase {
    suspend fun execute(authRequestModel: AuthRequestModel): Response<Boolean>
}
