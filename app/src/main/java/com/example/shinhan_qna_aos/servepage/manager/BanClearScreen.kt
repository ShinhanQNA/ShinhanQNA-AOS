package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.ManagerStudentInfo
import com.example.shinhan_qna_aos.TitleContentLike
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.TitleYearButton
import com.example.shinhan_qna_aos.TitleYearData
import com.example.shinhan_qna_aos.TopBar

@Composable
fun BanClearScreen(){
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
            TopBar("차단 해제 검토", {})
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

@Composable
fun BanClearDetailScreen(){
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar("", {}) // 타이틀 없을 땐 공백
        ManagerStudentInfo("이름", "홍길동", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp) // 좌우 간격 유지
                .fillMaxWidth(), // Row가 화면 전체를 차지하게
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ManagerStudentInfo("학번", "20221313", modifier = Modifier.weight(0.7f))
            ManagerStudentInfo("학년", "3", modifier = Modifier.weight(0.3f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        ManagerStudentInfo("학과", "소프트웨어융합", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn() {
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
}
@Preview(showBackground = true)
@Composable
fun BanClearPreview(){
//    BanClearScreen()
    BanClearDetailScreen()
}