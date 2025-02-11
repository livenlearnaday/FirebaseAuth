package io.github.livenlearnaday.firebaseauth.usecase

import androidx.credentials.Credential
import io.github.livenlearnaday.firebaseauth.util.Response

fun interface DeleteUserAccountUseCase {
    suspend fun execute(credential: Credential?): Response<Boolean>
}