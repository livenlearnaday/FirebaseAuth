package io.github.livenlearnaday.firebaseauth.usecase

import com.google.firebase.auth.AuthResult
import io.github.livenlearnaday.firebaseauth.util.Response

fun interface AnonymousSignInUseCase {
    suspend fun execute(): Response<AuthResult>
}