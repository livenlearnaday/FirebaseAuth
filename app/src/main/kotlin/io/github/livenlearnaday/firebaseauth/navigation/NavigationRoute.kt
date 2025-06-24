package io.github.livenlearnaday.firebaseauth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {
    @Serializable
    object HomeRoute

    @Serializable
    object LoginRoute

    @Serializable
    object ResetPasswordRoute
}
