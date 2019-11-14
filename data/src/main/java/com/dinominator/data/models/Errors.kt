package com.dinominator.data.models


data class AppErrors(
    val errorType: ErrorType = ErrorType.CONNECTION,
    val message: String? = null,
    val resId: Int? = null
) {
    enum class ErrorType {
        CONNECTION, TIME_OUT, OTHER
    }
}