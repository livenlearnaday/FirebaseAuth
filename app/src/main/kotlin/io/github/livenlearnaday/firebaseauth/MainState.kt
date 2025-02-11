package io.github.livenlearnaday.firebaseauth

import com.google.firebase.auth.FirebaseUser

data class MainState(
    val currentUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false

)