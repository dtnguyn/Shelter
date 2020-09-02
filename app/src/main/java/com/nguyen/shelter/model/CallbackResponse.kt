package com.nguyen.shelter.model

data class CallbackResponse<T>(
    val status: Boolean,
    val message: String = "",
    val data: T?
)