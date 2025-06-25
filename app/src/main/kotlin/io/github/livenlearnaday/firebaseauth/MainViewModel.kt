package io.github.livenlearnaday.firebaseauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetCurrentFirebaseUserUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class MainViewModel(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase
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
        MainViewModel::class.simpleName?.let {
            Timber.tag(it)
        }

        mainState = mainState.copy(
            isLoading = true
        )
    }

    fun mainAction(mainAction: MainAction) {
        when (mainAction) {
            MainAction.OnUpdateAuthState -> {
                updateAuthState()
            }
        }
    }

    private fun updateAuthState() {
        val response = getAuthStateUseCase.execute()
            .onStart {
                mainState = mainState.copy(
                    isLoading = true
                )
            }
            .onCompletion {
                mainState = mainState.copy(
                    isLoading = false
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), getCurrentFirebaseUserUseCase.execute())
        mainState = mainState.copy(
            currentUser = response.value,
            isLoggedIn = response.value != null,
            isLoading = false
        )
    }
}
