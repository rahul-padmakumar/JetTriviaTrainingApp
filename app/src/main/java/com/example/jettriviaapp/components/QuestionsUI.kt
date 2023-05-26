package com.example.jettriviaapp.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    val questionIndex = remember {
        mutableStateOf(0)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        when{
            data.value.isLoading == true ->  {
                CircularProgressIndicator()
            }
            data.value.data != null -> {

                val question = data.value.data?.get(questionIndex.value)
                val size = data.value.data?.size ?: 0

                MainQuestionDisplay(
                    question,
                    questionIndex,
                    size
                ){
                    if(questionIndex.value < (size - 1)) {
                        questionIndex.value = it + 1
                    }
                }
            }
            data.value.data != null -> {
                Text(text = data.value.exception?.message ?: "")
            }
        }
    }
}

@Composable
fun MainQuestionDisplay(
    result: Result?,
    index: MutableState<Int>,
    totalCount: Int,
    onNextClicked: (Int) -> Unit
) {

    val choicesState = remember(result) {
        result?.incorrect_answers?.toMutableList()?.apply {
            add(result.correct_answer)
        } ?: mutableListOf()
    }

    val answerState = remember(result){
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(result) {
        mutableStateOf<Boolean?>(null)
    }

    val scoreState = remember {
        mutableStateOf(0)
    }

    val updateAnswer: (Int) -> Unit = remember(result) {
        {
            if(it != answerState.value) {
                answerState.value = it
                correctAnswerState.value = choicesState[it] == result?.correct_answer
                if (correctAnswerState.value == true) {
                    scoreState.value = scoreState.value + 1
                } else if (correctAnswerState.value == false) {
                    if (scoreState.value > 0) {
                        scoreState.value = scoreState.value - 1
                    }
                }
            }
        }
    }

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
            if(scoreState.value > 0) {
                ShowProgress(score = scoreState.value, totalCount)
            }
            QuestionTracker(count = index.value + 1, outOf = totalCount)
            DottedDivider(pathEffect = pathEffect)
            Column{
                Text(
                    text = "${result?.question}",
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    color = TriviaColors.mOffWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                choicesState.forEachIndexed { index, s ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        TriviaColors.mOffDarkPurple,
                                        TriviaColors.mOffDarkPurple
                                    ),
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(
                                Color.Transparent
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = answerState.value == index,
                            onClick = { updateAnswer(index) },
                            modifier = Modifier.padding(
                                start = 16.dp
                            ),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if(correctAnswerState.value == true && index == answerState.value){
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if(correctAnswerState.value == true && answerState.value == index){
                                        Color.Green
                                    } else if(correctAnswerState.value == false && answerState.value == index){
                                        Color.Red
                                    } else {
                                        TriviaColors.mOffWhite
                                    },
                                    fontSize = 16.sp
                                )
                            ){
                                append(s)
                            }
                        }
                        Text(text =  annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }

                Button(
                    onClick = {onNextClicked(index.value)},
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = buttonColors(
                        backgroundColor = TriviaColors.mLightBlue
                    )
                ){
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = TriviaColors.mOffWhite,
                        fontSize = 16.sp
                    )
                }
            }

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

@Composable
fun ShowProgress(score: Int, totalCount: Int){

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF95075),
            Color(0xFFBE65EB)
        )
    )

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(TriviaColors.mOffDarkPurple, TriviaColors.mOffDarkPurple)
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .clip(
                RoundedCornerShape(
                    topEndPercent = 50,
                    topStartPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
    ){
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(score/totalCount.toFloat())
                .background(gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {
            Text(text = score.toString())
        }
    }
}