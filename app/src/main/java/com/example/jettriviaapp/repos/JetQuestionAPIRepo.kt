package com.example.jettriviaapp.repos

import com.example.jettriviaapp.data.DataOrException
import com.example.jettriviaapp.network.JetQuestionAPI
import javax.inject.Inject

class JetQuestionAPIRepo @Inject constructor(private val jetQuestionAPI: JetQuestionAPI){

    private var dataOrException: DataOrException<List<com.example.jettriviaapp.models.Result?>> =
        DataOrException()

    suspend fun getAllQuestions(): DataOrException<List<com.example.jettriviaapp.models.Result?>>{
        try {
            dataOrException = DataOrException(isLoading = true)
            val response = jetQuestionAPI.getQuestions().results
            dataOrException = if(response != null) {
                DataOrException(data = response, isLoading = false)
            } else {
                DataOrException(data = emptyList(), isLoading = false)
            }
        } catch (e: Exception){
            dataOrException = DataOrException(isLoading = false, exception = e)
        }

        return dataOrException
    }
}