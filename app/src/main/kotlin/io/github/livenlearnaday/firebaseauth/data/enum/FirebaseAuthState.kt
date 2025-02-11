package io.github.livenlearnaday.firebaseauth.data.enum

enum class FirebaseAuthState {
    Authenticated, // Anonymously authenticated in Firebase.
    SignedIn, // Authenticated in Firebase via a service provider, and not anonymous.
    SignedOut; // Not authenticated in Firebase.
}