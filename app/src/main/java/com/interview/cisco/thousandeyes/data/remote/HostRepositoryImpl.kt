package com.interview.cisco.thousandeyes.data.remote

import com.interview.cisco.thousandeyes.data.service.ApiService
import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.repo.HostRepository
import com.interview.cisco.thousandeyes.pinglib.PingLib
import com.interview.cisco.thousandeyes.utils.AppError
import com.interview.cisco.thousandeyes.utils.ResponseState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val pingLib: PingLib
): HostRepository {
    override fun getHostItems(): Flow<ResponseState<List<HostItem>>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val response = apiService.getHostItems().toMutableList()
            pingLib.pingHostsConcurrently(response.map { it.url }).forEach { pingResult ->
                response.forEachIndexed { index, hostItem ->
                    if(hostItem.url == pingResult.url){
                        response[index].apply {
                            this.success = pingResult.success
                            this.total = pingResult.total
                            this.failure = pingResult.failure
                            this.latency = pingResult.averageLatency
                        }
                    }
                }
            }
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(response))
        }catch (e: Exception){
            trySend(ResponseState.Loading())
            trySend(ResponseState.Error(AppError.GeneralError))
        }
        awaitClose {
            channel.close()
        }
    }

    override fun getHostPingResult(hostItem: HostItem): Flow<ResponseState<HostItem>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            pingLib.pingHost(hostItem.url).let { ping ->
                hostItem.apply {
                    total = ping.total
                    success = ping.success
                    failure = ping.failure
                    latency = ping.averageLatency
                }
            }
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(hostItem))
        }catch (e: Exception){
            trySend(ResponseState.Loading())
            trySend(ResponseState.Error(AppError.GeneralError))
        }
        awaitClose {
            channel.close()
        }
    }
}