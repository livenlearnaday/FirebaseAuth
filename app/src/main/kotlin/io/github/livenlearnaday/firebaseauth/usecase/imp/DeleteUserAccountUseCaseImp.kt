package io.github.livenlearnaday.firebaseauth.usecase.imp

import android.content.Context
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.withContext

class DeleteUserAccountUseCaseImp(
    private val authRepository: AuthRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : DeleteUserAccountUseCase {
    override suspend fun execute(context: Context, needReAuth: Boolean): Response<Boolean> = withContext(coroutineDispatcherProvider.io()) {
        if (needReAuth) {
            when (val response = authRepository.fetchCredential(context)) {
                is Response.Success -> {
                    response.data?.let { credential ->
                        authRepository.deleteUserAccount(credential)
                    } ?: authRepository.deleteUserAccount(null)
                }

                else -> {
                    authRepository.deleteUserAccount(null)
                }
            }
        } else {
            authRepository.deleteUserAccount(null)
        }
    }
}
