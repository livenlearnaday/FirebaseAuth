package io.github.livenlearnaday.firebaseauth

import android.app.Application
import io.github.livenlearnaday.firebaseauth.di.authModule
import io.github.livenlearnaday.firebaseauth.di.useCaseModule
import io.github.livenlearnaday.firebaseauth.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@MyApplication)
            modules(
                authModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
