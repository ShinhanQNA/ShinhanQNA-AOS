package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.ManagerButton
import com.example.shinhan_qna_aos.TopBar
import com.jihan.lucide_icons.lucide

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.shinhan_qna_aos.TitleContentLike
//import com.example.shinhan_qna_aos.TitleContentLikeButton
//import com.example.shinhan_qna_aos.TopBar

//@Composable
//fun DeclarationScreen(){
//    val dataList = listOf(
//        TitleContentLike("제목1", "본문내용1", 2, flagsCount = 1, banCount = 0),
//        TitleContentLike("제목2", "본문내용2", 5, flagsCount = 0, banCount = 2),
//        TitleContentLike("제목3", "본문내용3", 1, flagsCount = 2, banCount = 1),
//        TitleContentLike("제목4", "본문내용4", 1, flagsCount = 3, banCount = 4),
//        TitleContentLike("제목5", "본문내용5", 3, flagsCount = 0, banCount = 1),
//        TitleContentLike("제목6", "본문내용6", 8, flagsCount = 2, banCount = 2),
//        TitleContentLike("제목7", "본문내용7", 56, flagsCount = 6, banCount = 8),
//        TitleContentLike("제목8", "본문내용8", 185, flagsCount = 10, banCount = 15),
//    )
//    val isAdmin = true
//    Box {
//        Column {
//            TopBar("신고 검토", onNavigationClick = {})
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//                    .padding(bottom = 50.dp)
//            ) {
//                items(dataList) { data ->
//                    var responseState by remember { mutableStateOf(data.responseState) }
//                    TitleContentLikeButton(
//                        title = data.title,
//                        content = data.content,
//                        likeCount = data.likeCount,
//                        isAdmin = isAdmin,
//                        flagsCount = data.flagsCount,
//                        banCount = data.banCount,
//                        onResponseStateChange = { responseState = it },
//                        onClick = {}
//                    )
//                    Divider()
//                }
//            }
//        }
//        Text(
//            "배너광고",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(Color.Red)
//                .align(Alignment.BottomCenter)
//        )
//    }
//}
//
//@Composable
//fun DeclarationOpenScreen() {
//
//    Column(modifier = Modifier.systemBarsPadding().fillMaxSize()) {
//        TopBar(null) { navController.navigate("notices") { popUpTo("notices/${id}") { inclusive = true } } }
//        LazyColumn() {
//            item {
//                DetailContent(
//                    title =  "",
//                    content =  ""
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    ManagerButton(
//                        icon = lucide.arrow_big_left_dash,
//                        label = "반려",
//                        background = Color(0xffFC4F4F),
//                        onClick = { }
//                    )
//
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    ManagerButton(
//                        icon = lucide.flag,
//                        label = "경고",
//                        background = Color(0xffFF9F43),
//                        onClick = { }
//                    )
//                }
//            }
//        }
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun DeclarationPreview(){
//    DeclarationScreen()
//}