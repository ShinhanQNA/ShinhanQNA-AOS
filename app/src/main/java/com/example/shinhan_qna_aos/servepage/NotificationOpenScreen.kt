package com.example.shinhan_qna_aos.servepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.ui.theme.pretendard

@Composable
fun NotificationOpenScreen(){
    Box(){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            TopBar(null, {})
            DetailContent()
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

data class StringData(val content:String)
@Composable
fun DetailContent(){
    val datas = listOf( StringData(" Text(\n" +
            "            title,\n" +
            "            color = Color.Black,style = TextStyle(fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Bold,\n" +
            "                fontSize = 20.sp\n" +
            "            ),\n" +
            "        )\n" +
            "        Spacer(modifier = Modifier.height(8.dp))\n" +
            "        Text(\n" +
            "            content,\n" +
            "            color = Color(0xffA5A5A5),\n" +
            "            style = TextStyle(\n" +
            "                fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Normal,\n" +
            "                fontSize = 14.sp\n" +
            "            ),\n" +
            "            maxLines = 2,\n" +
            "            minLines = 2,\n" +
            "            overflow = TextOverflow.Ellipsis\n" +
            "        ) Text(\n" +
            "            title,\n" +
            "            color = Color.Black,\n" +
            "            style = TextStyle(\n" +
            "                fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Bold,\n" +
            "                fontSize = 20.sp\n" +
            "            ),\n" +
            "        )\n" +
            "        Spacer(modifier = Modifier.height(8.dp))\n" +
            "        Text(\n" +
            "            content,\n" +
            "            color = Color(0xffA5A5A5),\n" +
            "            style = TextStyle(\n" +
            "                fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Normal,\n" +
            "                fontSize = 14.sp\n" +
            "            ),\n" +
            "            maxLines = 2,\n" +
            "            minLines = 2,\n" +
            "            overflow = TextOverflow.Ellipsis\n" +
            "        ) Text(\n" +
            "            title,\n" +
            "            color = Color.Black,\n" +
            "            style = TextStyle(\n" +
            "                fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Bold,\n" +
            "                fontSize = 20.sp\n" +
            "            ),\n" +
            "        )\n" +
            "        Spacer(modifier = Modifier.height(8.dp))\n" +
            "        Text(\n" +
            "            content,\n" +
            "            color = Color(0xffA5A5A5),\n" +
            "            style = TextStyle(\n" +
            "                fontFamily = pretendard,\n" +
            "                fontWeight = FontWeight.Normal,\n" +
            "                fontSize = 14.sp\n" +
            "            ),\n" +
            "            maxLines = 2,\n" +
            "            minLines = 2,\n" +
            "            overflow = TextOverflow.Ellipsis\n" +
            "        )"
        )
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ){
        Text(
            "title",
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn ( modifier = Modifier.fillMaxWidth()){
            items(datas) {data ->
                Text(
                    data.content,
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    lineHeight = 28.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationOpenScreenPreview(){
    NotificationOpenScreen()
}