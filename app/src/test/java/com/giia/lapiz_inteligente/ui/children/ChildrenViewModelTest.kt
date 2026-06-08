package com.giia.lapiz_inteligente.ui.children

import com.giia.lapiz_inteligente.data.repository.ChildRepository
import com.giia.lapiz_inteligente.models.child.ChildResponse
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
class ChildrenViewModelTest {

    private val childRepository: ChildRepository = mockk()
    private lateinit var viewModel: ChildrenViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val sampleChild = ChildResponse(
        child_id = 1,
        user_id = 1,
        name = "Mateo",
        birth_date = null,
        dominant_hand = null,
        school_grade = null,
        notes = null,
        is_active = true,
        created_at = "2026-06-08T12:00:00"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init loads children and shows Success`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.success(listOf(sampleChild))

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is ChildUiState.Success)
        assert((state as ChildUiState.Success).children.size == 1)
        assert((state).children[0].name == "Mateo")
    }

    @Test
    fun `init shows Empty when no children`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.success(emptyList())

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.value is ChildUiState.Empty)
    }

    @Test
    fun `loadChildren shows Error on failure`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.failure(Exception("Network error"))

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is ChildUiState.Error)
        assert((state as ChildUiState.Error).message == "Network error")
    }

    @Test
    fun `createChild calls repository and reloads list`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.success(listOf(sampleChild))
        coEvery { childRepository.createChild(any(), any(), any(), any(), any()) } returns Result.success(sampleChild)

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var callbackCalled = false
        viewModel.createChild("Mateo") { callbackCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { childRepository.createChild(name = "Mateo", birthDate = null, dominantHand = null, schoolGrade = null, notes = null) }
        assert(callbackCalled)
    }

    @Test
    fun `updateChild calls repository and reloads list`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.success(listOf(sampleChild))
        coEvery { childRepository.updateChild(any(), any(), any(), any(), any(), any()) } returns Result.success(sampleChild)

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var callbackCalled = false
        viewModel.updateChild(1, "Mateo G.") { callbackCalled = true }
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { childRepository.updateChild(childId = 1, name = "Mateo G.", birthDate = null, dominantHand = null, schoolGrade = null, notes = null) }
        assert(callbackCalled)
    }

    @Test
    fun `deactivateChild calls repository and reloads list`() = runTest(testDispatcher) {
        coEvery { childRepository.getChildren() } returns Result.success(listOf(sampleChild))
        coEvery { childRepository.deactivateChild(any()) } returns Result.success(sampleChild)

        viewModel = ChildrenViewModel(childRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.deactivateChild(1)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { childRepository.deactivateChild(1) }
    }
}
