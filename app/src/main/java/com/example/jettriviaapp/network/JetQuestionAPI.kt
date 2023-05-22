package com.example.jettriviaapp.network

import com.example.jettriviaapp.models.JetTriviaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JetQuestionAPI {
    @GET("api.php")
    suspend fun getQuestions(@Query("amount") amount: Int = 10): JetTriviaResponse
}