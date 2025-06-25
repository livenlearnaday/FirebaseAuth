package io.github.livenlearnaday.firebaseauth.usecase

import com.google.firebase.auth.FirebaseUser

fun interface GetCurrentFirebaseUserUseCase {
    fun execute(): FirebaseUser?
}
