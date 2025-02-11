package io.github.livenlearnaday.firebaseauth.usecase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

fun interface GetAuthStateUseCase {
    fun execute(scope: CoroutineScope): StateFlow<FirebaseUser?>
}