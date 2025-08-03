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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.TitleContentLike
import com.example.shinhan_qna_aos.TitleContentLikeButton

@Composable
fun SaySomthingScreen() {
    val dataList = listOf(
        TitleContentLike("제목1", "본문내용1", 2, flagsCount = 1, banCount = 0),
        TitleContentLike("제목2", "본문내용2", 5, flagsCount = 0, banCount = 2),
        TitleContentLike("제목3", "본문내용3", 1, flagsCount = 2, banCount = 1),
        TitleContentLike("제목4", "본문내용4", 1, flagsCount = 3, banCount = 4),
        TitleContentLike("제목5", "본문내용5", 3, flagsCount = 0, banCount = 1),
        TitleContentLike("제목6", "본문내용6", 8, flagsCount = 2, banCount = 2),
        TitleContentLike("제목7", "본문내용7", 56, flagsCount = 6, banCount = 8),
        TitleContentLike("제목8", "본문내용8", 185, flagsCount = 10, banCount = 15),
    )
    val isAdmin = true // ViewModel 등 실제 로그인 상태에 따라 분기!

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White).padding(bottom = 50.dp)) {
        items(dataList) { data ->
            // 리스트 각각의 응답상태를 기억(로컬 컴포즈 state)
            var responseState by remember { mutableStateOf(data.responseState) }
            TitleContentLikeButton(
                title = data.title,
                content = data.content,
                likeCount = data.likeCount,
                isAdmin = isAdmin,
                flagsCount = data.flagsCount,
                banCount = data.banCount,
                onResponseStateChange = { responseState = it }
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