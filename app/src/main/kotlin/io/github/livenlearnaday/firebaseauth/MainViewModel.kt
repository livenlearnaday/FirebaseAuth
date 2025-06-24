package io.github.livenlearnaday.firebaseauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class MainViewModel(
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    companion object {
        @JvmStatic
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.Forest.e(throwable)
    }

    var mainState by mutableStateOf(MainState())
        private set

    init {
        mainState = mainState.copy(
            isLoading = true
        )

        updateAuthState()

        mainState = mainState.copy(
            isLoading = false
        )
    }

    private fun updateAuthState() {
        val response = getAuthStateUseCase.execute(viewModelScope)
        mainState = mainState.copy(
            currentUser = response.value,
            isLoggedIn = response.value != null
        )
    }
}
