package com.example.jettriviaapp.data

data class DataOrException<T>(
    val data: T? = null,
    val isLoading: Boolean? = null,
    val exception: Exception? = null
)
