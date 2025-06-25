package io.github.livenlearnaday.firebaseauth.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

class StandardTestDispatcherProvider(
    testCoroutineScheduler: TestCoroutineScheduler
) : CoroutineDispatcherProvider {
    private val dispatcher = StandardTestDispatcher(testCoroutineScheduler)

    override fun io(): CoroutineDispatcher = dispatcher

    override fun main(): CoroutineDispatcher = dispatcher

    override fun unconfined(): CoroutineDispatcher = dispatcher

    override fun default(): CoroutineDispatcher = dispatcher
}
