//package com.example.shinhan_qna_aos.etc.user
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.shinhan_qna_aos.TitleContent
//import com.example.shinhan_qna_aos.TitleContentButton
//import com.example.shinhan_qna_aos.TopBar
//
//@Composable
//fun MyWriteScreen(){
//    val dataList = listOf( // 임의 값
//        TitleContent("제목1", "본문내용1"),
//        TitleContent("제목2", "본문내용2",),
//        TitleContent("제목3", "본문내용3", ),
//        TitleContent("제목4", "본문내용4", ),
//        TitleContent("제목5", "본문내용5", ),
//        TitleContent("제목6", "본문내용6", ),
//        TitleContent("제목7", "본문내용7", ),
//        TitleContent("제목8", "본문내용8", )
//    )
//    Column {
//        TopBar("내가 작성한 게시글",{})
//        Box(){
//            LazyColumn(modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 50.dp)) {
//                items(dataList) { data ->
//                    TitleContentButton(
//                        title = data.title,
//                        content = data.content,
//                    )
//                    Divider()
//                }
//            }
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
//@Preview(showBackground = true)
//@Composable
//fun MyWritePreview(){
//    MyWriteScreen()
////}