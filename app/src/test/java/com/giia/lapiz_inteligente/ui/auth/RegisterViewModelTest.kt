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
class RegisterViewModelTest {

    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register starts with Idle state`() {
        assert(viewModel.uiState.value is AuthUiState.Idle)
    }

    @Test
    fun `register sets Loading then Success on success`() = runTest(testDispatcher) {
        coEvery { authRepository.register(any(), any(), any(), any()) } returns Result.success(mockk())

        viewModel.register("Juan", "Perez", "juan@example.com", "password123")

        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.uiState.value is AuthUiState.Success)
        coVerify { authRepository.register("Juan", "Perez", "juan@example.com", "password123") }
    }

    @Test
    fun `register sets Error on failure`() = runTest(testDispatcher) {
        coEvery { authRepository.register(any(), any(), any(), any()) } returns Result.failure(Exception("Email already registered"))

        viewModel.register("Juan", "Perez", "existing@example.com", "password123")

        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is AuthUiState.Error)
        assert((state as AuthUiState.Error).message == "Email already registered")
    }

    @Test
    fun `resetState returns to Idle`() {
        viewModel.resetState()
        assert(viewModel.uiState.value is AuthUiState.Idle)
    }
}
