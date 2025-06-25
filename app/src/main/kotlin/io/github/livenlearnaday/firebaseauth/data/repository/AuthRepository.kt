package io.github.livenlearnaday.firebaseauth.data.repository

import android.content.Context
import androidx.credentials.Credential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.util.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    fun getAuthState(): Flow<FirebaseUser?>

    suspend fun signInAnonymously(): Response<AuthResult>

    suspend fun fetchCredential(context: Context): Response<Credential>

    suspend fun signInWithGoogle(credential: Credential): Response<AuthResult>

    suspend fun signOut(): Response<Boolean>

    suspend fun deleteUserAccount(credential: Credential?): Response<Boolean>

    fun checkNeedsReAuth(): Boolean

    suspend fun resetPassword(email: String): Response<Boolean>

    suspend fun loginWithEmailAndPassword(authRequestModel: AuthRequestModel): Response<Boolean>

    suspend fun signUpWithEmailAndPassword(authRequestModel: AuthRequestModel): Response<Boolean>
}
