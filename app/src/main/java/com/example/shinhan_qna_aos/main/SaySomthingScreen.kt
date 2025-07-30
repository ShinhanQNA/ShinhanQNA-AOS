package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun SaySomthingScreen() {
    val dataList = listOf( // 임의 값
        SayData("제목1", "본문내용1", 2),
        SayData("제목2", "본문내용2", 5),
        SayData("제목3", "본문내용3", 1),
        SayData("제목4", "본문내용4", 1),
        SayData("제목5", "본문내용5", 3),
        SayData("제목6", "본문내용6", 8),
        SayData("제목7", "본문내용7", 56),
        SayData("제목8", "본문내용8", 185)

    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)) {
        items(dataList) { data ->
            SayList(
                title = data.title,
                content = data.content,
                likeCount = data.likeCount
            )
            Divider()
        }
    }
}

// 데이터 클래스 임의
data class SayData(val title: String, val content: String, val likeCount: Int)

@Composable
fun SayList(title: String, content: String, likeCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(title,
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(content,
            color = Color(0xffA5A5A5),
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(lucide.thumbs),
                contentDescription = "좋아요 표시",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                likeCount.toString(),
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SayScreenPreview(){
    SaySomthingScreen()
//    SayList()
}