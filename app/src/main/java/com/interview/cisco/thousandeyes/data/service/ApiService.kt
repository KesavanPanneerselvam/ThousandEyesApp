package com.interview.cisco.thousandeyes.data.service

import com.interview.cisco.thousandeyes.domain.model.HostItem
import retrofit2.http.GET

interface ApiService {
    @GET("sk_hosts")
    suspend fun getHostItems(): List<HostItem>
}