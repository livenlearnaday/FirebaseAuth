package io.github.livenlearnaday.firebaseauth.di

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepositoryImp
import io.github.livenlearnaday.firebaseauth.util.Constants.WEB_CLIENT_ID
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import io.github.livenlearnaday.firebaseauth.util.createNonce
import io.github.livenlearnaday.firebaseauth.util.imp.CoroutineDispatcherProviderImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun provideGoogleIdOption() =  GetGoogleIdOption.Builder()
    .setFilterByAuthorizedAccounts(false)
    .setServerClientId(WEB_CLIENT_ID)
    .setNonce(createNonce())
    .setAutoSelectEnabled(false)
    .build()


fun provideCredentialRequest() = GetCredentialRequest.Builder()
    .addCredentialOption(provideGoogleIdOption())
    .build()

fun provideFirebaseInstance() = FirebaseAuth.getInstance()


val authModule = module {

    single<FirebaseAuth> {
        provideFirebaseInstance()
    }

    single<CredentialManager> {
        CredentialManager.create(androidContext())
    }

    single<GetCredentialRequest> {
        provideCredentialRequest()
    }

    factory<AuthRepository> {
        AuthRepositoryImp(
            get<FirebaseAuth>(),
            get<CredentialManager>(),
            get<GetCredentialRequest>()
        )
    }

    factory<CoroutineDispatcherProvider> {
        CoroutineDispatcherProviderImp()
    }


}