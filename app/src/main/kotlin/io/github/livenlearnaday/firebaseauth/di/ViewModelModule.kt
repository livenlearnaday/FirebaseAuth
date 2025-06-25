package io.github.livenlearnaday.firebaseauth.di

import io.github.livenlearnaday.firebaseauth.MainViewModel
import io.github.livenlearnaday.firebaseauth.auth.login.LoginViewModel
import io.github.livenlearnaday.firebaseauth.auth.reset.ResetPasswordViewModel
import io.github.livenlearnaday.firebaseauth.home.HomeViewModel
import io.github.livenlearnaday.firebaseauth.usecase.AnonymousSignInUseCase
import io.github.livenlearnaday.firebaseauth.usecase.DeleteUserAccountUseCase
import io.github.livenlearnaday.firebaseauth.usecase.FetchCredentialUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetCurrentFirebaseUserUseCase
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

    viewModel<MainViewModel> {
        MainViewModel(
            get<GetAuthStateUseCase>(),
            get<GetCurrentFirebaseUserUseCase>()
        )
    }

    viewModel<LoginViewModel> {
        LoginViewModel(
            get<CoroutineDispatcherProvider>(),
            get<GetAuthStateUseCase>(),
            get<AnonymousSignInUseCase>(),
            get<GoogleSignInUseCase>(),
            get<SignUpWithEmailAndPasswordUseCase>(),
            get<LogInWithEmailAndPasswordUseCase>(),
            get<FetchCredentialUseCase>(),
            get<GetCurrentFirebaseUserUseCase>()
        )
    }

    viewModel<HomeViewModel> {
        HomeViewModel(
            get<CoroutineDispatcherProvider>(),
            get<GetAuthStateUseCase>(),
            get<ReAuthenticationCheckUseCase>(),
            get<SignOutUseCase>(),
            get<DeleteUserAccountUseCase>(),
            get<GetCurrentFirebaseUserUseCase>()
        )
    }

    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel(
            get<ResetPasswordUseCase>()
        )
    }
}
