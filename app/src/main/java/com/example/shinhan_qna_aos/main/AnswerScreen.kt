package com.example.shinhan_qna_aos.main

//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.shinhan_qna_aos.TitleContent
//import com.example.shinhan_qna_aos.TitleContentButton

//@Composable
//fun AnsweredScreen() {
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
//    LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)) {
//        items(dataList) { data ->
//            TitleContentButton(
//                title = data.title,
//                content = data.content,
//            )
//            Divider()
//        }
//    }
//}
//@Composable
//@Preview(showBackground = true)
//fun AnsweredPreview(){
//    AnsweredScreen()
//}