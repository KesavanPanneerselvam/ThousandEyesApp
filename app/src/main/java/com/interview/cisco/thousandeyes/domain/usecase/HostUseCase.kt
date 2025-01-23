package com.interview.cisco.thousandeyes.domain.usecase

import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.repo.HostRepository
import com.interview.cisco.thousandeyes.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HostUseCase @Inject constructor(
    private val repository: HostRepository
) {
    fun getHostItems(): Flow<ResponseState<List<HostItem>>> = repository.getHostItems()

    fun getHostPingResult(hostItem: HostItem): Flow<ResponseState<HostItem>> = repository.getHostPingResult(hostItem)
}