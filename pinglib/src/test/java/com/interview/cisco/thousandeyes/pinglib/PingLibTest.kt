package com.interview.cisco.thousandeyes.pinglib

import io.mockk.*
import io.mockk.coEvery
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.InetAddress


@OptIn(ExperimentalCoroutinesApi::class)
class PingLibTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `pingHost should return success for reachable host`() = runTest {
        // Mock InetAddress behavior
        mockkStatic(InetAddress::class)
        val address = "127.0.0.1"
        val expectedCount = 5

        coEvery { InetAddress.getByName(address).isReachable(any()) } returns true

        val result = PingLib.pingHost(address)

        assertEquals(address, result.url)
        assertEquals(expectedCount, result.total)
        assertEquals(expectedCount, result.success)
        assertEquals(0, result.failure)
        assertEquals(expectedCount, result.success + result.failure) // Sanity check

        unmockkStatic(InetAddress::class)
    }

    @Test
    fun `pingHost should return failure for unreachable host`() = runTest {
        // Mock InetAddress behavior
        mockkStatic(InetAddress::class)
        val address = "192.168.0.123"
        val expectedCount = 3

        coEvery { InetAddress.getByName(address).isReachable(any()) } returns false

        val result = PingLib.pingHost(address, expectedCount)

        assertEquals(address, result.url)
        assertEquals(expectedCount, result.total)
        assertEquals(0, result.success)
        assertEquals(expectedCount, result.failure)
        assertEquals(expectedCount, result.success + result.failure) // Sanity check

        unmockkStatic(InetAddress::class)
    }

    @Test
    fun `pingHost should handle exceptions gracefully`() = runTest {
        // Mock InetAddress behavior
        mockkStatic(InetAddress::class)
        val address = "invalid-host"
        val expectedCount = 3

        coEvery { InetAddress.getByName(address) } throws Exception("Invalid address")

        val result = PingLib.pingHost(address, expectedCount)

        assertEquals(address, result.url)
        assertEquals(expectedCount, result.total)
        assertEquals(0, result.success)
        assertEquals(expectedCount, result.failure)

        unmockkStatic(InetAddress::class)
    }

    @Test
    fun `pingHostsConcurrently should return results for multiple hosts`() = runTest {
        // Mock InetAddress behavior
        mockkStatic(InetAddress::class)
        val addresses = listOf("127.0.0.1", "192.168.0.123", "invalid-host")
        val expectedCount = 3

        coEvery { InetAddress.getByName("127.0.0.1").isReachable(any()) } returns true
        coEvery { InetAddress.getByName("192.168.0.123").isReachable(any()) } returns false
        coEvery { InetAddress.getByName("invalid-host") } throws Exception("Invalid address")

        val results = PingLib.pingHostsConcurrently(addresses, expectedCount)

        assertEquals(expectedCount, results.size)

        // Assertions for each result
        val successResult = results[0]
        assertEquals("127.0.0.1", successResult.url)
        assertEquals(expectedCount, successResult.total)
        assertEquals(expectedCount, successResult.success)
        assertEquals(0, successResult.failure)

        val failureResult = results[1]
        assertEquals("192.168.0.123", failureResult.url)
        assertEquals(expectedCount, failureResult.total)
        assertEquals(0, failureResult.success)
        assertEquals(expectedCount, failureResult.failure)

        val exceptionResult = results[2]
        assertEquals("invalid-host", exceptionResult.url)
        assertEquals(expectedCount, exceptionResult.total)
        assertEquals(0, exceptionResult.success)
        assertEquals(expectedCount, exceptionResult.failure)

        unmockkStatic(InetAddress::class)
    }
}