package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TitleContentLike
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.login.TokenManager

@Composable
fun SaySomthingScreen() {
    val context = LocalContext.current
    val viewModel = remember {
        SaySomtingViewModel(
            tokenManager = TokenManager(context) // 또는 다른 방식으로 TokenManager 생성
        )
    }

    val dataList = viewModel.postList
    val isAdmin = viewModel.isAdmin

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 50.dp)
    ) {
        items(dataList, key = { it.postID }) { data ->
            // 리스트 각각의 응답 상태를 로컬 상태로 관리
            var responseState by remember { mutableStateOf(data.responseState) }
            TitleContentLikeButton(
                title = data.title,
                content = data.content,
                likeCount = data.likeCount,
                isAdmin = isAdmin,
                flagsCount = data.flagsCount,
                banCount = data.banCount,
                responseState = responseState,
                onResponseStateChange = { newState ->
                    responseState = newState
                    // 관리자인 경우 필요하면 API 호출 연동 등 추가 가능
                }
            )
            Divider()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SayScreenPreview(){
    SaySomthingScreen()
}