package com.interview.cisco.thousandeyes.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel: ViewModel() {
    val showLoading = mutableStateOf(false)
    val showSuccessMessage = mutableStateOf(DisplayMessage())
    val showErrorMessage = mutableStateOf(DisplayMessage())

    fun backgroundScope(block:suspend ()->Unit){
        viewModelScope.launch {
            block.invoke()
        }
    }

    fun showProgressBar(boolean: Boolean){
        showLoading.value = boolean
    }

    fun processError(e: Exception?) = showErrorMessage(e?.message ?: "")

    private fun showErrorMessage(message: String){
        showErrorMessage.value = DisplayMessage(message = message, enable = true)
        resetMessage(showErrorMessage)
    }

    suspend fun uiScope(block:suspend ()->Unit){
        withContext(Dispatchers.Main){
            block.invoke()
        }
    }

    private fun resetMessage(item: MutableState<DisplayMessage>){
        backgroundScope {
            delay(1500)
            uiScope {
                item.value = DisplayMessage()
            }
        }
    }
}