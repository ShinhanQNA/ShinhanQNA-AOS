package com.example.shinhan_qna_aos.main

//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.shinhan_qna_aos.SelectData
//import com.example.shinhan_qna_aos.SelectDataButton
//import com.example.shinhan_qna_aos.TitleContentLike
//import com.example.shinhan_qna_aos.TitleContentLikeButton
//import com.example.shinhan_qna_aos.TopBar
//import com.example.shinhan_qna_aos.ui.theme.pretendard
//import com.jihan.lucide_icons.lucide

//@Composable
//fun SelectedOpinionsScreen() {
//    val dataList = listOf( // 임의 값
//        SelectData(2003,3,2,9),
//        SelectData(2003,3,2,9),
//        SelectData(2003,3,2,9),
//        SelectData(2003,3,2,9),
//        SelectData(2003,3,2,9 ),
//        SelectData(2003,3,2,9 ),
//        SelectData(2003,3,2,9),
//        SelectData(2003,3,2,9)
//    )
//    val isAdmin = true
//    val responseOptions = listOf("대기", "응답중", "응답 완료")
//    LazyColumn(modifier = Modifier
//        .fillMaxSize()
//        .padding(bottom = 50.dp)){
//        items(dataList) { data ->
//            var responseState by remember { mutableStateOf(data.responseState) }
//            SelectDataButton(
//                year = data.year,
//                month = data.month,
//                week = data.week,
//                count = data.count,
//                isAdmin = isAdmin,
//                onResponseStateChange = { responseState = it }
//            )
//            Divider()
//        }
//    }
//}
//
//@Composable
//fun SelectedDetailScreen() {
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
//    val responseOptions = listOf("대기", "응답중", "응답 완료")
//    Box {
//        Column {
//            TopBar("nnnn년 mm월 i주차", onNavigationClick = {})
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp)
//                    .background(Color.White),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                ) {
//                    LazyRow(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(end = 70.dp)
//                    ) {
//                        item {
//                            FilterToggleButton("전체", selected = true, onClick = { })
//                            Spacer(modifier = Modifier.width(8.dp))
//                            FilterToggleButton("1학년", selected = false, onClick = { })
//                            Spacer(modifier = Modifier.width(8.dp))
//                            FilterToggleButton("2학년", selected = false, onClick = { })
//                            Spacer(modifier = Modifier.width(8.dp))
//                            FilterToggleButton("3학년", selected = false, onClick = { })
//                            Spacer(modifier = Modifier.width(8.dp))
//                            FilterToggleButton("4학년", selected = false, onClick = { })
//                        }
//                    }
//                    // 오른쪽 끝 페이드 아웃 효과 추가
//                    EdgeFadeOverlay(
//                        color = Color.White, // 배경색과 맞추기
//                        width = 130.dp,
//                        modifier = Modifier.align(Alignment.CenterEnd)
//                    )
//                }
//                // 정렬/내보내기
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        painter = painterResource(lucide.filter),
//                        contentDescription = "정렬",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(6.dp))
//                    Text(
//                        "정렬",
//                        color = Color.Black,
//                        style = TextStyle(
//                            fontFamily = pretendard,
//                            fontWeight = FontWeight.Normal,
//                            fontSize = 14.sp
//                        )
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        painter = painterResource(lucide.file_text),
//                        contentDescription = "내보내기",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(6.dp))
//                    Text(
//                        "내보내기",
//                        color = Color.Black,
//                        style = TextStyle(
//                            fontFamily = pretendard,
//                            fontWeight = FontWeight.Normal,
//                            fontSize = 14.sp
//                        )
//                    )
//                }
//            }
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
//fun EdgeFadeOverlay(
//    color: Color,
//    width: Dp,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .width(width)
//            .height(36.dp)
//            .background(
//                brush = Brush.horizontalGradient(
//                    colors = listOf(
//                        Color.Transparent,
//                        color.copy(alpha = 0.85f),
//                        color
//                    ),
//                )
//            )
//    )
//}
//
//
//@Composable
//fun FilterToggleButton(
//    text: String,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(12.dp))
//            .height(36.dp)
//            .background(if (selected) Color.Black else Color.White)
//            .border(
//                1.dp,
//                if (selected) Color.Black else Color(0xFFDADADA),
//                RoundedCornerShape(12.dp)
//            )
//            .clickable { onClick() }
//            .padding(horizontal = 12.dp, vertical = 8.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = if (selected) Color.White else Color.Black,
//            fontSize = 14.sp,
//            fontFamily = pretendard,
//            fontWeight = FontWeight.Normal
//        )
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun SelectedOpinionsPreview(){
//    SelectedOpinionsScreen()
////    SelectedDetailScreen()
//}