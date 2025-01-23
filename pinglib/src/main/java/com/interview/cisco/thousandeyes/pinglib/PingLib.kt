package com.interview.cisco.thousandeyes.pinglib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.system.measureTimeMillis

object PingLib {

    suspend fun pingHost(address: String, pingCount: Int = 5): PingResult =
        withContext(Dispatchers.IO) {
            val times = mutableListOf<Long>()
            repeat(pingCount) {
                val time = measureTimeMillis {
                    try {
                        if (!InetAddress.getByName(address)
                                .isReachable(1000)
                        ) return@withContext processResult(address, pingCount, times)
                    } catch (e: Exception) {
                        return@withContext processResult(address, pingCount, times)
                    }
                }
                times.add(time)
            }
            return@withContext processResult(address, pingCount, times)
        }

    private fun processResult(address: String, pingCount: Int, times: List<Long>): PingResult = PingResult(
        address,
        if(times.isNotEmpty()) times.average().toLong() else null,
        pingCount,
        times.size,
        pingCount - times.size
    )

    suspend fun pingHostsConcurrently(urls: List<String>, pingCount: Int = 5): List<PingResult> = coroutineScope {
        urls.map { url ->
            async {
                pingHost(url, pingCount)
            }
        }.awaitAll()
    }
}

data class PingResult(
    val url: String,
    val averageLatency: Long?,
    val total: Int,
    val success: Int,
    val failure: Int
)