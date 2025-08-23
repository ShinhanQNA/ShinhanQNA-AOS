package com.example.shinhan_qna_aos.servepage

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.shinhan_qna_aos.Data
//import com.example.shinhan_qna_aos.DetailContent
//import com.example.shinhan_qna_aos.TitleContent
//import com.example.shinhan_qna_aos.TitleContentButton
//import com.example.shinhan_qna_aos.TopBar
//import com.example.shinhan_qna_aos.ui.theme.pretendard
//import com.jihan.lucide_icons.lucide
//
//@Composable
//fun NotificationScreen(data: Data) {
//    Column {
//        TopBar("공지", {})
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .systemBarsPadding()
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 50.dp)
//            ) {
//                items(dataList) { data ->
//                    TitleContentButton(
//                        title = data.title,
//                        content = data.content,
//                    )
//                    Divider()
//                }
//            }
//
//            if(data.isAdmin){// + 새공지 버튼 - 배너 바로 위 공간에 위치하도록 아래 패딩 추가
//                Button(
//                    onClick = { /* 새공지 클릭 동작 */ },
//                    shape = RoundedCornerShape(6.dp),
//                    colors = ButtonDefaults.buttonColors(Color.Black),
//                    contentPadding = PaddingValues(0.dp),
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(bottom = 66.dp, end = 20.dp) // 배너 위 공간 + 여백 확보
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(6.dp),
//                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(lucide.plus),
//                            contentDescription = "새공지 추가",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Text(
//                            "새공지",
//                            color = Color.White,
//                            style = TextStyle(
//                                fontFamily = pretendard,
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 14.sp
//                            )
//                        )
//                    }
//                }
//            }
//
//            // 하단 배너 광고
//            Text(
//                "배너광고",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .background(Color.Red)
//                    .align(Alignment.BottomCenter)
//            )
//        }
//    }
//}
//
//@Composable
//fun NotificationOpenScreen(){
//    Box(){
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(bottom = 50.dp)
//        ) {
//            TopBar(null, {})
//            DetailContent(title = "ㅇ", content = "ㄴㄴ")
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
//@Preview(showBackground = true)
//@Composable
//fun NotificationOpenScreenPreview(){
//    NotificationOpenScreen()
//}
//
//@Preview(showBackground = true)
//@Composable
//fun NotificationPreview(){
////    NotificationScreen()
//}