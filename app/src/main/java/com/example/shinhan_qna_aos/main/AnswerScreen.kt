package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TitleContentButton
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.main.api.Answer
import com.example.shinhan_qna_aos.main.api.AnswerRepository
import com.example.shinhan_qna_aos.main.api.AnswerViewModel

@Composable
fun AnsweredScreen(answerRepository: AnswerRepository, navController: NavController) {
    val answerViewModel: AnswerViewModel =
        viewModel(factory = SimpleViewModelFactory { AnswerViewModel(answerRepository) })

    val answerList by answerViewModel.answerList.collectAsState()
    // 최초에 데이터 로드
    LaunchedEffect(Unit) {
        answerViewModel.loadAnswers()
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 50.dp)) {
        items(answerList, key = {it.id}) { answer ->
            TitleContentButton(
                title = answer.title,
                content = answer.content,
                onClick = { navController.navigate("answerOpen/${answer.id}") }
            )
            Divider()
        }
    }
}

@Composable
fun AnsweredOpenScreen(
    answerRepository: AnswerRepository,
    navController: NavController,
    id: Int
) {
    val answerViewModel: AnswerViewModel =
        viewModel(factory = SimpleViewModelFactory { AnswerViewModel(answerRepository) })

    val selectedAnswer by answerViewModel.selectedAnswer.collectAsState()
    val answerList by answerViewModel.answerList.collectAsState()

    // 최초에 리스트가 비어있거나 id가 바뀌면 답변 리스트 로드 후 id 검색
    LaunchedEffect(id, answerList) {
        if (answerList.isEmpty()) {
            answerViewModel.loadAnswers()
        }
        answerViewModel.selectAnswerById(id)
    }

    Box(
        modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            selectedAnswer?.let { answer ->

                TopBar(null)
                {
                    navController.navigate("main?selectedTab=2") {
                        popUpTo("answerOpen/${answer.id}") { inclusive = true }
                    }
                }
                DetailContent(title = answer.title, content = answer.content)
            }
        }

        Text(
            "배너광고",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Red)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AnsweredPreview(){
//    AnsweredScreen()
}