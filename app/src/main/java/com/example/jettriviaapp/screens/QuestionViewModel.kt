package com.example.jettriviaapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettriviaapp.data.DataOrException
import com.example.jettriviaapp.repos.JetQuestionAPIRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionAPIRepo: JetQuestionAPIRepo): ViewModel() {

    val data: MutableStateFlow<DataOrException<List<com.example.jettriviaapp.models.Result?>>> =
        MutableStateFlow(DataOrException(null, true, null))

    init {
        getQuestions()
    }

    private fun getQuestions(){
        viewModelScope.launch {
            data.tryEmit(DataOrException(isLoading = true))
            data.tryEmit(questionAPIRepo.getAllQuestions())
        }
    }
}