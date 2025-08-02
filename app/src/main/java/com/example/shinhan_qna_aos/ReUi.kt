package com.example.shinhan_qna_aos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

// 데이터 클래스 임의
data class TitleContentLike(
    val title: String,
    val content: String,
    val likeCount: Int,
    val flagsCount: Int = 0,
    val banCount: Int = 0,
    val responseState: String = "응답 상태"
)

data class TitleContent(val title: String, val content: String)
data class SelectData(val year: Int,val month: Int, val week: Int, val count: Int)
data class StringData(val content:String)

@Composable
fun TitleContentLikeButton(
    title: String,
    content: String,
    likeCount: Int,
    isAdmin: Boolean,
    flagsCount: Int,
    banCount: Int,
    responseState: String,
    responseOptions: List<String> = listOf("대기", "응답중", "응답 완료"),
    onResponseStateChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽: 게시글 본문 영역
        Column {
            Text(
                title,
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
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
                Spacer(modifier = Modifier.width(8.dp))
                if (isAdmin) {
                    // 신고 수
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.flag),
                            contentDescription = "신고 표시",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xffFF9F43)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            flagsCount.toString(),
                            color = Color(0xffFF9F43),
                            style = TextStyle(
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            ),
                        )
                    }
                    // 차단 수
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(lucide.ban),
                            contentDescription = "차단 표시",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xffFC4F4F)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            banCount.toString(),
                            color = Color(0xffFC4F4F),
                            style = TextStyle(
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            ),
                        )
                    }
                }
            }
        }

        // 오른쪽: 관리자 응답 상태 드롭다운
        if (isAdmin) {
            Box {
                Row(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        // responseState가 기본 값이면 회색, 아니면 검정
                        text = if (responseState.isBlank() || responseState == "응답 상태") "응답 상태" else responseState,
                        fontSize = 13.sp,
                        fontFamily = pretendard,
                        color =  Color.Black
                    )
                    Icon(
                        painter = painterResource(lucide.chevron_down),
                        contentDescription = "응답 상태 선택",
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    responseOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, fontFamily = pretendard, fontSize = 14.sp) },
                            onClick = {
                                onResponseStateChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TitleContentButton(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            title,
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            content,
            color = Color(0xffA5A5A5),
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SelectDataButton(year: Int,month:Int, week:Int, count:Int){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "${year}년 ${month}월 ${week}주차",
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "의견 ${count}개",
            color = Color(0xffA5A5A5),
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(Color(0xffFF9F43), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(R.drawable.ellipse),
                    contentDescription = "원",
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "대기",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title : String?,
    onNavigationClick: () -> Unit
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)) {
                Text(
                    text = title ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = lucide.chevron_left),
                contentDescription = "Navigation Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigationClick() }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White),
        colors = TopAppBarDefaults.topAppBarColors(Color.White)
    )
}

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
            "            style = TextStyle(\n"
         )
    )
    Column (
        modifier = Modifier
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

@Composable
@Preview(showBackground = true)
fun ReUiPreview(){
//    var response by remember { mutableStateOf("응답 상태") }
//
//    TitleContentLikeButton(
//        title = "테스트 제목",
//        content = "테스트 본문내용",
//        likeCount = 10,
//        isAdmin = false,
//        flagsCount = 2,
//        banCount = 1,
//        responseState = response,
//        responseOptions = listOf("대기", "응답중", "응답 완료"),
//        onResponseStateChange = { response = it }
//    )

//    TopBar("공지",{})
//    Spacer(modifier = Modifier.height(16.dp))
//    UserLikeButton(45)
//    DetailContent()
}