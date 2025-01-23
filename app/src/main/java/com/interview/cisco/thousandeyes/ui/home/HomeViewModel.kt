package com.interview.cisco.thousandeyes.ui.home

import androidx.compose.runtime.mutableStateOf
import com.interview.cisco.thousandeyes.core.BaseViewModel
import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.domain.usecase.HostUseCase
import com.interview.cisco.thousandeyes.utils.ResponseState
import com.interview.cisco.thousandeyes.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hostUseCase: HostUseCase
) : BaseViewModel() {

    private var _hostItems: MutableStateFlow<UIState<List<HostItem>>> =
        MutableStateFlow(UIState.Init)

    val hostItems: StateFlow<UIState<List<HostItem>>> get() = _hostItems

    var sortByName = mutableStateOf(false)

    fun getHostList() {
        _hostItems.value = UIState.Init
        backgroundScope {
            hostUseCase.getHostItems().collect { response ->
                uiScope {
                    when (response) {
                        is ResponseState.Loading -> showProgressBar(response.isLoading)
                        is ResponseState.Success -> {
                            _hostItems.value = UIState.Success(response.data)
                        }

                        is ResponseState.Error -> {
                            showProgressBar(false)
                            processError(response.e)
                            _hostItems.value = UIState.Error(response.e?.message ?: "")
                        }
                    }
                }
            }
        }
    }

    fun getHostDetails(index: Int, hostList: MutableList<HostItem>) {
        _hostItems.value = UIState.Init
        backgroundScope {
            hostUseCase.getHostPingResult(hostList[index]).collect { response ->
                uiScope {
                    when (response) {
                        is ResponseState.Loading -> showProgressBar(response.isLoading)
                        is ResponseState.Success -> {
                            response.data.let {
                                hostList[index] = HostItem(
                                    name = it.name,
                                    url = it.url,
                                    icon = it.icon,
                                    total = it.total,
                                    success = it.success,
                                    failure = it.failure,
                                    latency = it.latency
                                )
                            }
                            _hostItems.value = UIState.Success(hostList.toList())
                        }

                        is ResponseState.Error -> {
                            showProgressBar(false)
                            processError(response.e)
                            _hostItems.value = UIState.Error(response.e?.message ?: "")
                        }
                    }
                }
            }
        }
    }
}