package com.interview.cisco.thousandeyes.ui.home

import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.usecase.HostUseCase
import com.interview.cisco.thousandeyes.utils.AppError
import com.interview.cisco.thousandeyes.utils.ResponseState
import com.interview.cisco.thousandeyes.utils.UIState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var viewModel: HomeViewModel
    private lateinit var hostUseCase: HostUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Initialize Main dispatcher
        hostUseCase = mockk()
        viewModel = HomeViewModel(hostUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after test
        unmockkAll() // Unmock all mockk objects
    }

    @Test
    fun `getHostList should emit Init and Success states`() = testScope.runTest {
        // Arrange
        val mockHostItems = listOf(
            HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png"),
            HostItem(name = "Host 2", url = "host2.com", icon = "icon2.png")
        )
        coEvery { hostUseCase.getHostItems() } returns flow {
            emit(ResponseState.Loading(true))
            emit(ResponseState.Success(mockHostItems))
        }

        val emittedStates = mutableListOf<UIState<List<HostItem>>>()

        // Act
        val job = launch {
            viewModel.hostItems.collect { emittedStates.add(it) }
        }

        viewModel.getHostList()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all tasks complete

        // Assert
        assertEquals(2, emittedStates.size) // Expect Init and Success states
        assertEquals(UIState.Init, emittedStates[0])
        assertTrue(emittedStates[1] is UIState.Success)

        val successState = emittedStates[1] as UIState.Success
        assertEquals(mockHostItems, successState.data)

        // Cancel flow collection
        job.cancel()
    }

    @Test
    fun `getHostList should emit Init and Error states`() = testScope.runTest {
        // Arrange
        val mockError = AppError.GeneralError
        coEvery { hostUseCase.getHostItems() } returns flow {
            emit(ResponseState.Loading(true))
            emit(ResponseState.Error(mockError))
        }

        // Act
        viewModel.getHostList()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Error(mockError.message ?: ""), viewModel.hostItems.value)
    }

    @Test
    fun `getHostDetails should emit Init and Success states with updated details`() = testScope.runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png")
        val updatedHostItem = mockHostItem.copy(latency = 100, total = 5, success = 5, failure = 0)
        val hostList = mutableListOf(mockHostItem)

        coEvery { hostUseCase.getHostPingResult(mockHostItem) } returns flow {
            emit(ResponseState.Loading(true))
            emit(ResponseState.Success(updatedHostItem))
        }

        // Act
        viewModel.getHostDetails(0, hostList)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Success(listOf(updatedHostItem)), viewModel.hostItems.value)
    }

    @Test
    fun `getHostDetails should emit Init and Error states`() = testScope.runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png")
        val hostList = mutableListOf(mockHostItem)
        val mockError = AppError.GeneralError

        coEvery { hostUseCase.getHostPingResult(mockHostItem) } returns flow {
            emit(ResponseState.Loading(true))
            emit(ResponseState.Error(mockError))
        }

        // Act
        viewModel.getHostDetails(0, hostList)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UIState.Error(mockError.message ?: ""), viewModel.hostItems.value)
    }
}

