package io.github.livenlearnaday.firebaseauth.home

import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.enum.FirebaseAuthState

data class HomeState(
    val currentUser: FirebaseUser? = null,
    val shouldNavigateToLogin: Boolean = false,
    val openDeleteAccountDialog: Boolean = false,
    val firebaseAuthState: FirebaseAuthState = FirebaseAuthState.SignedOut,
    val isLogOutSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isDeletingAccount: Boolean = false,
    val errorMessage: String = "",
    val showError: Boolean = false,
    val showAuthOptions: Boolean = false
)
