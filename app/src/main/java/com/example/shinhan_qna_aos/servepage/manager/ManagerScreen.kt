package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.Caution
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun ManagerScreen (){
    Box(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .background(Color.White)
        ) {
            TopBar("관리자", {})
            ManagerButton()
            Spacer(modifier = Modifier.height(16.dp))
            Caution()
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
fun ManagerDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar("", {}) // 타이틀 없을 땐 공백
        ManagerDetailInfoHalf("이름", "홍길동", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp) // 좌우 간격 유지
                .fillMaxWidth(), // Row가 화면 전체를 차지하게
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ManagerDetailInfoHalf("학번", "20221313", modifier = Modifier.weight(0.7f))
            ManagerDetailInfoHalf("학년", "3", modifier = Modifier.weight(0.3f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        ManagerDetailInfoHalf("학과", "소프트웨어융합", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "재학 확인서 첨부(학생증, 재학증명서)",
            style = TextStyle(
                color = Color.Black,
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp) // 좌우 간격 유지
         ) {
            item {
                Image(
                    painterResource(R.drawable.android_mockup1),
                    contentDescription = "재학증명서"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)){
                    Button(
                        onClick = {},
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xffFC4F4F)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(lucide.x),
                                contentDescription = "거절",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "거절",
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                ),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {},
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xff4AD871)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "승인",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Text(
                                "승인",
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManagerDetailInfoHalf(title: String, info: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            title,
            style = TextStyle(
                color = Color.Black,
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xffdfdfdf), RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                info,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color.Black
                ),
            )
        }
    }
}

@Composable
fun ManagerButton(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 35.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "가입 요청 검토",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Icon(
                painter = painterResource(lucide.arrow_right),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "신고 검토",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Icon(
                painter = painterResource(lucide.arrow_right),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "차단 해제 검토",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Icon(
                painter = painterResource(lucide.arrow_right),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clickable { },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "로그아웃",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerPreview(){
//    ManagerScreen()
    ManagerDetailScreen()
}