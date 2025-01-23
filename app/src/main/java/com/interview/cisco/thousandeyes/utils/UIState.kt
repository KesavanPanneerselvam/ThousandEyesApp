package com.interview.cisco.thousandeyes.utils

sealed class UIState<out T> {
    object Init : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
}