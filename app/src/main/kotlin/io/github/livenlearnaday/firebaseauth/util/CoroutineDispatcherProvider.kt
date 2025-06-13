package io.github.livenlearnaday.firebaseauth.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatcherProvider {
    fun io(): CoroutineDispatcher = Dispatchers.IO

    fun default(): CoroutineDispatcher = Dispatchers.Default

    fun main(): CoroutineDispatcher = Dispatchers.Main

    fun immediate(): CoroutineDispatcher = Dispatchers.Main.immediate

    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}
