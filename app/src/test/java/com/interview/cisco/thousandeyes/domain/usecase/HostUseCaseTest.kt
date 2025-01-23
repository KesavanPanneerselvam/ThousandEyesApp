package com.interview.cisco.thousandeyes.domain.usecase

import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.repo.HostRepository
import com.interview.cisco.thousandeyes.utils.AppError
import com.interview.cisco.thousandeyes.utils.ResponseState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class HostUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: HostRepository
    private lateinit var usecase: HostUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        usecase = HostUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getHostItems should delegate to repository and emit success`() = testScope.runTest {
        // Arrange
        val mockHostItems = listOf(
            HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png", latency = 100, success = 5),
            HostItem(name = "Host 2", url = "host2.com", icon = "icon2.png", latency = 200, success = 4)
        )

        coEvery { repository.getHostItems() } returns flowOf(ResponseState.Success(mockHostItems))

        val emittedStates = usecase.getHostItems().first()

        // Assert
        assertTrue(emittedStates is ResponseState.Success)

        val successState = emittedStates as ResponseState.Success
        val updatedItems = successState.data
        assertEquals(2, updatedItems.size)
        assertEquals(100L, updatedItems[0].latency)
        assertEquals(5, updatedItems[0].success)
        assertEquals(200L, updatedItems[1].latency)
        assertEquals(4, updatedItems[1].success)

        coVerify { repository.getHostItems() }
    }

    @Test
    fun `getHostItems should emit error on repository failure`() = runTest {
        // Arrange
        coEvery { repository.getHostItems() } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = usecase.getHostItems()

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(GENERAL_ERROR, error?.message)
    }

    @Test
    fun `getHostPingResult should delegate to repository and emit success`() = runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png")
        val updatedHostItem = mockHostItem.copy(latency = 100, total = 5, success = 5, failure = 0)

        coEvery { repository.getHostPingResult(mockHostItem) } returns flowOf(ResponseState.Success(updatedHostItem))

        // Act
        val result = usecase.getHostPingResult(mockHostItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Success)
        val data = (emittedState as ResponseState.Success).data
        assertEquals("host1.com", data.url)
        assertEquals(100L, data.latency)
        assertEquals(5, data.success)
    }

    @Test
    fun `getHostPingResult should emit error on repository failure`() = runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png")
        coEvery { repository.getHostPingResult(mockHostItem) } returns flowOf(ResponseState.Error(AppError.GeneralError))

        // Act
        val result = usecase.getHostPingResult(mockHostItem)

        // Assert
        val emittedState = result.first()
        assertTrue(emittedState is ResponseState.Error)
        val error = (emittedState as ResponseState.Error).e
        assertEquals(GENERAL_ERROR, error?.message)
    }

    companion object{
        const val GENERAL_ERROR = "Something went wrong. Please try again later."
    }
}