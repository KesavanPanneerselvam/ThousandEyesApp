package com.interview.cisco.thousandeyes.utils

sealed class ResponseState<out T> {
    data class Loading(val isLoading: Boolean = false): ResponseState<Nothing>()

    data class Success<out T>(
        val data: T
    ): ResponseState<T>()

    data class Error(
        val e: AppError? = AppError.GeneralError
    ): ResponseState<Nothing>()
}

sealed class AppError(message: String): Exception(message){
    data object GeneralError: AppError(GENERAL_ERROR)
    data class CustomError(val error: String): AppError(error)
    data object UnknownError: AppError(UNKNOWN_ERROR)
    companion object{
        const val GENERAL_ERROR = "Something went wrong. Please try again later."
        const val UNKNOWN_ERROR = "Unknown Error."
    }
}