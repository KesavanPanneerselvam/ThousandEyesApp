package com.interview.cisco.thousandeyes.domain.usecase

import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.repo.HostRepository
import javax.inject.Inject

class HostUseCase @Inject constructor(
    private val repository: HostRepository
) {
    fun getHostItems() = repository.getHostItems()

    fun getHostPingResult(hostItem: HostItem) = repository.getHostPingResult(hostItem)
}