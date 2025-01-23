package com.interview.cisco.thousandeyes.data.remote

import com.interview.cisco.thousandeyes.data.service.ApiService
import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.pinglib.PingLib
import com.interview.cisco.thousandeyes.pinglib.PingResult
import com.interview.cisco.thousandeyes.utils.AppError
import com.interview.cisco.thousandeyes.utils.ResponseState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class HostRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var repository: HostRepositoryImpl
    private lateinit var apiService: ApiService
    private lateinit var pingLib: PingLib

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        pingLib = mockk()
        apiService = mockk()
        repository = HostRepositoryImpl(apiService, pingLib)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getHostItems should emit success with updated host items`() = testScope.runTest {
        // Arrange
        val mockHostItems = listOf(
            HostItem(name = "Host 1", url = "host1.com", icon = "icon1.png"),
            HostItem(name = "Host 2", url = "host2.com", icon = "icon2.png")
        )
        val mockPingResults = listOf(
            PingResult(
                url = "host1.com",
                averageLatency = 100,
                total = 5,
                success = 5,
                failure = 0
            ),
            PingResult(
                url = "host2.com",
                averageLatency = 200,
                total = 5,
                success = 4,
                failure = 1
            )
        )

        coEvery { apiService.getHostItems() } returns mockHostItems
        coEvery { pingLib.pingHostsConcurrently(any()) } returns mockPingResults

        val emittedStates = mutableListOf<ResponseState<List<HostItem>>>()

        // Act
        val job = launch(testDispatcher) {
            repository.getHostItems().collect { emittedStates.add(it) }
        }

        // Move the dispatcher to run the queued coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(3, emittedStates.size)
        assertEquals(ResponseState.Loading(isLoading = true), emittedStates[0])
        assertEquals(ResponseState.Loading(isLoading = false), emittedStates[1])
        assertTrue(emittedStates[2] is ResponseState.Success)

        val successState = emittedStates[2] as ResponseState.Success
        val updatedItems = successState.data
        assertEquals(2, updatedItems.size)
        assertEquals(100L, updatedItems[0].latency)
        assertEquals(5, updatedItems[0].success)
        assertEquals(200L, updatedItems[1].latency)
        assertEquals(4, updatedItems[1].success)

        coVerify { apiService.getHostItems() }
        coVerify { pingLib.pingHostsConcurrently(listOf("host1.com", "host2.com")) }

        // Ensure the job is canceled after collection
        job.cancel()
    }

    @Test
    fun `getHostItems should emit error on exception`() = testScope.runTest {
        // Arrange
        coEvery { apiService.getHostItems() } throws Exception("Network error")

        // Act
        val emittedStates = mutableListOf<ResponseState<List<HostItem>>>()

        val job = launch(testDispatcher) {
            repository.getHostItems().collect { emittedStates.add(it) }
        }

        // Move the dispatcher to run the queued coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(3, emittedStates.size)
        assertEquals(ResponseState.Loading(isLoading = true), emittedStates[0])
        assertEquals(ResponseState.Loading(isLoading = false), emittedStates[1])
        assertTrue(emittedStates[2] is ResponseState.Error)

        val error = (emittedStates[2] as ResponseState.Error).e
        assertEquals(AppError.GeneralError, error)

        job.cancel()
    }

    @Test
    fun `getHostPingResult should emit success with updated ping results`() = testScope.runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1")
        val mockPingResult = PingResult(
            url = "host1.com",
            averageLatency = 150,
            total = 5,
            success = 5,
            failure = 0
        )

        coEvery { pingLib.pingHost("host1.com") } returns mockPingResult

        // Act
        val emittedStates = mutableListOf<ResponseState<HostItem>>()

        val job = launch(testDispatcher) {
            repository.getHostPingResult(mockHostItem).collect { emittedStates.add(it) }
        }

        // Move the dispatcher to run the queued coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(3, emittedStates.size)
        assertEquals(ResponseState.Loading(isLoading = true), emittedStates[0])
        assertEquals(ResponseState.Loading(isLoading = false), emittedStates[1])
        assertTrue(emittedStates[2] is ResponseState.Success)

        val updatedItem = (emittedStates[2] as ResponseState.Success).data as HostItem

        assertEquals("host1.com", updatedItem.url)
        assertEquals(150L, updatedItem.latency)
        assertEquals(5, updatedItem.success)

        job.cancel()
    }

    @Test
    fun `getHostPingResult should emit error on exception`() = testScope.runTest {
        // Arrange
        val mockHostItem = HostItem(name = "Host 1", url = "host1.com", icon = "icon1")

        coEvery { pingLib.pingHost("host1.com") } throws Exception("Ping error")

        // Act
        val emittedStates = mutableListOf<ResponseState<HostItem>>()

        val job = launch(testDispatcher) {
            repository.getHostPingResult(mockHostItem).collect { emittedStates.add(it) }
        }

        // Move the dispatcher to run the queued coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(3, emittedStates.size)
        assertEquals(ResponseState.Loading(isLoading = true), emittedStates[0])
        assertEquals(ResponseState.Loading(isLoading = false), emittedStates[1])
        assertTrue(emittedStates[2] is ResponseState.Error)

        val error = (emittedStates[2] as ResponseState.Error).e
        assertEquals(AppError.GeneralError, error)

        job.cancel()
    }
}