package io.github.livenlearnaday.firebaseauth.usecase

import android.content.Context
import io.github.livenlearnaday.firebaseauth.util.Response

fun interface DeleteUserAccountUseCase {
    suspend fun execute(context: Context, needReAuth: Boolean): Response<Boolean>
}
