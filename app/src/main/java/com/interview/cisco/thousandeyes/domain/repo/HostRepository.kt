package com.interview.cisco.thousandeyes.domain.repo

import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface HostRepository {
    fun getHostItems(): Flow<ResponseState<List<HostItem>>>
    fun getHostPingResult(hostItem: HostItem): Flow<ResponseState<HostItem>>
}