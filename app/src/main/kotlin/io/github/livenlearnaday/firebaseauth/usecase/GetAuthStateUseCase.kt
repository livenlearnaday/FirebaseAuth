package io.github.livenlearnaday.firebaseauth.usecase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

fun interface GetAuthStateUseCase {
    fun execute(): Flow<FirebaseUser?>
}
