package io.github.livenlearnaday.firebaseauth.di

import io.github.livenlearnaday.firebaseauth.data.repository.AuthRepository
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
import io.github.livenlearnaday.firebaseauth.usecase.imp.AnonymousSignInUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.DeleteUserAccountUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.FetchCredentialUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.GetAuthStateUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.GoogleSignInUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.LogInWithEmailAndPasswordUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.ReAuthenticationCheckUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.ResetPasswordUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.SignOutUseCaseImp
import io.github.livenlearnaday.firebaseauth.usecase.imp.SignUpWithEmailAndPasswordUseCaseImp
import org.koin.dsl.module

val useCaseModule = module {

    factory<AnonymousSignInUseCase> {
        AnonymousSignInUseCaseImp(get<AuthRepository>())
    }

    factory<GoogleSignInUseCase> {
        GoogleSignInUseCaseImp(get<AuthRepository>())
    }

    factory<GetAuthStateUseCase> {
        GetAuthStateUseCaseImp(get<AuthRepository>())
    }

    factory<ReAuthenticationCheckUseCase> {
        ReAuthenticationCheckUseCaseImp(get<AuthRepository>())
    }

    factory<SignOutUseCase> {
        SignOutUseCaseImp(get<AuthRepository>())
    }

    factory<DeleteUserAccountUseCase> {
        DeleteUserAccountUseCaseImp(get<AuthRepository>())
    }

    factory<ResetPasswordUseCase> {
        ResetPasswordUseCaseImp(get<AuthRepository>())
    }

    factory<SignUpWithEmailAndPasswordUseCase> {
        SignUpWithEmailAndPasswordUseCaseImp(get<AuthRepository>())
    }

    factory<LogInWithEmailAndPasswordUseCase> {
        LogInWithEmailAndPasswordUseCaseImp(get<AuthRepository>())
    }

    factory<FetchCredentialUseCase> {
        FetchCredentialUseCaseImp(get<AuthRepository>())
    }
}
