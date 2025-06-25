package io.github.livenlearnaday.firebaseauth.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.github.livenlearnaday.firebaseauth.data.model.AuthRequestModel
import io.github.livenlearnaday.firebaseauth.util.Response
import io.github.livenlearnaday.firebaseauth.util.isWithinPast
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest
) : AuthRepository {

    companion object {
        @JvmStatic
        private val TAG = AuthRepositoryImp::class.simpleName
    }

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun getAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
            Timber.d("$TAG getAuthState User: ${auth.currentUser?.uid ?: "Not authenticated"}")
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signInAnonymously(): Response<AuthResult> = try {
        val authResult = auth.signInAnonymously().await()
        authResult?.user?.let { user ->
            Timber.d("$TAG signInAnonymously FirebaseAuthSuccess: Anonymous UID: ${user.uid}")
        }
        Response.Success(authResult)
    } catch (error: Exception) {
        Timber.d("$TAG signInAnonymously FirebaseAuthError: Failed to Sign in anonymously")
        Response.Failure(error)
    }

    override suspend fun fetchCredential(context: Context): Response<Credential> = try {
        val result = credentialManager.getCredential(
            context = context,
            request = getCredentialRequest
        )
        Response.Success(result.credential)
    } catch (e: NoCredentialException) {
        e.printStackTrace()
        Timber.e("$TAG fetchCredential NoCredentialException $e")
        Response.Failure(e)
    } catch (e: GetCredentialException) {
        e.printStackTrace()
        Timber.e("$TAG fetchCredential GetCredentialException $e")
        Response.Failure(e)
    }

    override suspend fun signInWithGoogle(credential: Credential): Response<AuthResult> = when (credential) {
        // GoogleIdToken credential
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken
                    val firebaseCredential =
                        GoogleAuthProvider.getCredential(googleIdToken, null)

                    val authResult = when (auth.currentUser?.isAnonymous) {
                        true -> auth.currentUser?.linkWithCredential(firebaseCredential)
                            ?.await()

                        else -> auth.signInWithCredential(firebaseCredential).await()
                    }

                    Response.Success(authResult)
                } catch (e: GoogleIdTokenParsingException) {
                    Timber.e("$TAG signInWithGoogle Received an invalid google id token response $e")
                    Response.Failure(e)
                } catch (e: Exception) {
                    Timber.e("$TAG signInWithGoogle Unexpected error $e")
                    Response.Failure(e)
                }
            } else {
                Timber.e("$TAG signInWithGoogle Unexpected type of custom credential")
                val exception =
                    Exception(Throwable(message = "error occurred. CustomCredential credential type is not google."))
                Response.Failure(exception)
            }
        }

        else -> {
            // Catch any unrecognized credential type here.
            Timber.e("$TAG signInWithGoogle Unexpected type of credential")
            val exception =
                Exception(Throwable(message = "error occurred. credential type is not google."))
            Response.Failure(exception)
        }
    }

    override suspend fun deleteUserAccount(credential: Credential?): Response<Boolean> = try {
        when (credential) {
            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken

                    // Re-authenticate if needed
                    if (checkNeedsReAuth()) {
                        reAuthenticate(googleIdToken)
                    }
                    // Revoke
                    auth.signOut()
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                }
            }
        }
        // Delete firebase user
        auth.currentUser?.delete()?.await()
        Response.Success(true)
        Timber.e("$TAG deleteUserAccount FirebaseAuthError: Current user is not available")
        Response.Success(false)
    } catch (e: Exception) {
        Timber.e("$TAG deleteUserAccount FirebaseAuthError: Failed to delete user")
        Response.Failure(e)
    }

    override fun checkNeedsReAuth(): Boolean {
        auth.currentUser?.metadata?.lastSignInTimestamp?.let { lastSignInTimeStamp ->
            Timber.d("$TAG checkNeedsReAuth $lastSignInTimeStamp")
            return !lastSignInTimeStamp.isWithinPast(5)
        }
        return false
    }

    override suspend fun resetPassword(email: String): Response<Boolean> = try {
        var isCompleted = false
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isCompleted = true
                } else {
                    isCompleted = false
                }
            }.await()

        if (isCompleted) {
            Response.Success(true)
        } else {
            Response.Success(false)
        }
    } catch (e: Exception) {
        Timber.e("$TAG resetPassword Failed to reset password")
        Response.Failure(e)
    }

    override suspend fun loginWithEmailAndPassword(authRequestModel: AuthRequestModel): Response<Boolean> = try {
        auth.signInWithEmailAndPassword(authRequestModel.email, authRequestModel.password)
            .await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun signUpWithEmailAndPassword(authRequestModel: AuthRequestModel): Response<Boolean> = try {
        auth.createUserWithEmailAndPassword(authRequestModel.email, authRequestModel.password)
            .await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun signOut(): Response<Boolean> = try {
        auth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        Response.Success(true)
    } catch (e: java.lang.Exception) {
        Response.Failure(e)
    }

    private suspend fun reAuthenticate(googleIdToken: String) {
        val googleCredential = GoogleAuthProvider
            .getCredential(googleIdToken, null)
        auth.currentUser?.reauthenticate(googleCredential)?.await()
    }
}
