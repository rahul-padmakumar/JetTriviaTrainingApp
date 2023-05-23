package com.example.jettriviaapp.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviaapp.data.DataOrException
import com.example.jettriviaapp.models.Result
import com.example.jettriviaapp.utils.TriviaColors

@Composable
fun QuestionsUI(data: State<DataOrException<List<Result?>>>){

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        when{
            data.value.isLoading == true ->  {
                CircularProgressIndicator()
            }
            data.value.data != null -> {
                MainQuestionDisplay()
            }
            data.value.data != null -> {
                Text(text = data.value.exception?.message ?: "")
            }
        }
    }
}

@Composable
fun MainQuestionDisplay(){

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = TriviaColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            DottedDivider(pathEffect = pathEffect)
        }
    }
}

@Composable
fun QuestionTracker(count: Int = 10, outOf: Int = 100){
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle()){
            withStyle(style = SpanStyle(
                color = TriviaColors.mLightPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )){
                append("Questions $count/")

                withStyle(style = SpanStyle(
                    color = TriviaColors.mLightPurple,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )){
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun DottedDivider(pathEffect: PathEffect){
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ){
        drawLine(
            color = TriviaColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}