package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.SelectData
import com.example.shinhan_qna_aos.SelectDataButton
import com.example.shinhan_qna_aos.TitleContentLike
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.TopBar

@Composable
fun SelectedOpinionsScreen() {
    val dataList = listOf( // 임의 값
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9 ),
        SelectData(2003,3,2,9 ),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9)
    )
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 50.dp)){
        items(dataList) { data ->
            SelectDataButton(
                year = data.year,
                month = data.month,
                week = data.week,
                count = data.count
            )
            Divider()
        }
    }
}

@Composable
fun SelectedDetailScreen() {
    val dataList = listOf( // 임의 값
        TitleContentLike("제목1", "본문내용1", 2),
        TitleContentLike("제목2", "본문내용2", 5),
        TitleContentLike("제목3", "본문내용3", 1),
        TitleContentLike("제목4", "본문내용4", 1),
        TitleContentLike("제목5", "본문내용5", 3),
        TitleContentLike("제목6", "본문내용6", 8),
        TitleContentLike("제목7", "본문내용7", 56),
        TitleContentLike("제목8", "본문내용8", 185)

    )
    Column(){
        TopBar("nnnn년 mm월 i주차", onNavigationClick = {})
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)) {
            items(dataList) { data ->
                TitleContentLikeButton(
                    title = data.title,
                    content = data.content,
                    likeCount = data.likeCount
                )
                Divider()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SelectedOpinionsPreview(){
//    SelectedOpinionsScreen()
    SelectedDetailScreen()
}