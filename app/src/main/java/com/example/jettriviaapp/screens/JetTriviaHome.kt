package com.example.jettriviaapp.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jettriviaapp.components.QuestionsUI

@Composable
fun JetTriviaHome(viewModel: QuestionViewModel = hiltViewModel()) {

    QuestionsUI(data = viewModel.data.collectAsStateWithLifecycle())

}

