package io.github.livenlearnaday.firebaseauth.usecase

import androidx.credentials.Credential
import com.google.firebase.auth.AuthResult
import io.github.livenlearnaday.firebaseauth.util.Response

fun interface GoogleSignInUseCase {
    suspend fun execute(credential: Credential): Response<AuthResult>
}
