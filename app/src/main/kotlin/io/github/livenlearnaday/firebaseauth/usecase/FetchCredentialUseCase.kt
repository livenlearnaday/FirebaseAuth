package io.github.livenlearnaday.firebaseauth.usecase

import android.content.Context
import androidx.credentials.Credential
import io.github.livenlearnaday.firebaseauth.util.Response

fun interface FetchCredentialUseCase {
    suspend fun execute(context: Context): Response<Credential>
}