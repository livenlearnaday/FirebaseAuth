package io.github.livenlearnaday.firebaseauth

sealed interface MainAction {
    data object OnUpdateAuthState : MainAction
}
