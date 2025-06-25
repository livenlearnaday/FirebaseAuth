package io.github.livenlearnaday.firebaseauth

import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.livenlearnaday.firebaseauth.usecase.GetAuthStateUseCase
import io.github.livenlearnaday.firebaseauth.usecase.GetCurrentFirebaseUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val getAuthStateUseCase: GetAuthStateUseCase = mockk()
    private val getCurrentFirebaseUserUseCase: GetCurrentFirebaseUserUseCase = mockk()
    private lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setup() {
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseUser::class)
        mockFirebaseAuth = mockk()
        mockFirebaseUser = mockk()
        every { FirebaseAuth.getInstance() } returns mockFirebaseAuth
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        viewModel = MainViewModel(getAuthStateUseCase, getCurrentFirebaseUserUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testUpdateAuthStateShouldReturnLogin() {
        // Arrange
        every { getCurrentFirebaseUserUseCase.execute() } returns mockFirebaseUser
        every { getAuthStateUseCase.execute() } returns flowOf(mockFirebaseUser)

        // Act
        viewModel.mainAction(MainAction.OnUpdateAuthState)

        //Assert
        if (!viewModel.mainState.isLoading) {
            println("viewModel.mainState.currentUser: ${viewModel.mainState.currentUser}")
            assertThat(viewModel.mainState.isLoggedIn).isTrue()
        }
    }
}
