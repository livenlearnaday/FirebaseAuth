package io.github.livenlearnaday.firebaseauth.usecase.imp

import android.content.Context
import androidx.credentials.Credential
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.FetchCredentialUseCase
import io.github.livenlearnaday.firebaseauth.util.Response

class FetchCredentialUseCaseImp(
    private val authRepository: AuthRepository
) : FetchCredentialUseCase {
    override suspend fun execute(context: Context): Response<Credential> = authRepository.fetchCredential(context)
}
