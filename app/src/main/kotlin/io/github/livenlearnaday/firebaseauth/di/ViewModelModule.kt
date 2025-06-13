package io.github.livenlearnaday.firebaseauth.di

import io.github.livenlearnaday.firebaseauth.auth.AuthViewModel
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordViewModel
import io.github.livenlearnaday.firebaseauth.usecase.AnonymousSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.usecase.FetchCredentialUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GoogleSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.LogInWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.usecase.ReAuthenticationCheckUseCase
import io.github.livenlearnaday.firebaseauth.usecase.ResetPasswordUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignOutUseCase
import io.github.livenlearnaday.firebaseauth.usecase.SignUpWithEmailAndPasswordUseCase
import io.github.livenlearnaday.firebaseauth.util.CoroutineDispatcherProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<AuthViewModel> {
        AuthViewModel(
            get<CoroutineDispatcherProvider>(),
            get<GetAuthStateUseCase>(),
            get<AnonymousSignInUseCase>(),
            get<GoogleSignInUseCase>(),
            get<ReAuthenticationCheckUseCase>(),
            get<SignOutUseCase>(),
            get<DeleteUserAccountUseCase>(),
            get<SignUpWithEmailAndPasswordUseCase>(),
            get<LogInWithEmailAndPasswordUseCase>(),
            get<FetchCredentialUseCase>()
        )
    }

    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel(
            get<ResetPasswordUseCase>()
        )
    }
}
