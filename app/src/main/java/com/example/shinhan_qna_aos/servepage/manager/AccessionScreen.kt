package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.TitleYearButton
import com.example.shinhan_qna_aos.TitleYearData
import com.example.shinhan_qna_aos.TopBar

@Composable
fun AccessionScreen (){
    val dataList = listOf(
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
    )
    Box(){
        Column() {
            TopBar("가입 요청 검토", {})
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 50.dp)
            ) {
                items(dataList) { data ->
                    TitleYearButton(
                        data.name,
                        data.studentid,
                        data.grade,
                        data.major,
                        data.year,
                        data.month,
                        data.day
                    )
                    Divider()
                }
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

@Preview(showBackground = true)
@Composable
fun AccessionPreview(){
    AccessionScreen()
}