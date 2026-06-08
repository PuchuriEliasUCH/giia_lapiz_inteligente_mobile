package com.giia.lapiz_inteligente.ui.auth

import com.giia.lapiz_inteligente.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login starts with Idle state`() = runTest(testDispatcher) {
        assert(viewModel.uiState.value is AuthUiState.Idle)
    }

    @Test
    fun `login sets Loading then Success on success`() = runTest(testDispatcher) {
        coEvery { authRepository.login(any(), any()) } returns Result.success(mockk())

        viewModel.login("test@example.com", "password")

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value is AuthUiState.Success)
        coVerify { authRepository.login("test@example.com", "password") }
    }

    @Test
    fun `login sets Error on failure`() = runTest(testDispatcher) {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Invalid credentials"))

        viewModel.login("test@example.com", "wrong")

        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is AuthUiState.Error)
        assert((state as AuthUiState.Error).message == "Invalid credentials")
    }

    @Test
    fun `resetState returns to Idle`() {
        viewModel.resetState()
        assert(viewModel.uiState.value is AuthUiState.Idle)
    }
}
