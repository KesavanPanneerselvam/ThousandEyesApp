package com.interview.cisco.thousandeyes.domain.model

data class HostItem(
    val name: String,
    val url: String,
    val icon: String,
    var total: Int = 0,
    var success: Int = 0,
    var failure: Int = 0,
    var latency: Long? = null
)