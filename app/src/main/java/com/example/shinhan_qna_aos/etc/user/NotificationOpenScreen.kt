package com.example.shinhan_qna_aos.etc.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.TopBar

@Composable
fun NotificationOpenScreen(){
    Box(){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            TopBar(null, {})
            DetailContent(title = "ㅇ", content = "ㄴㄴ")
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

@Preview(showBackground = true)
@Composable
fun NotificationOpenScreenPreview(){
    NotificationOpenScreen()
}