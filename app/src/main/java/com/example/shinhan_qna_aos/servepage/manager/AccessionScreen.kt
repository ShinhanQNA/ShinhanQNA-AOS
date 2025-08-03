package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.ManagerStudentInfo
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.TitleYearButton
import com.example.shinhan_qna_aos.TitleYearData
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun AccessionScreen (){
    val dataList = listOf(
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
        TitleYearData("이름", "학번","학년","전공", 2240,  3, 10),
    )
    Box(){
        Column() {
            TopBar("가입 요청 검토", {})
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 50.dp)
            ) {
                items(dataList) { data ->
                    TitleYearButton(
                        data.name,
                        data.studentid,
                        data.grade,
                        data.major,
                        data.year,
                        data.month,
                        data.day
                    )
                    Divider()
                }
            }
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
fun AccessionDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar("", {}) // 타이틀 없을 땐 공백
        ManagerStudentInfo("이름", "홍길동", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp) // 좌우 간격 유지
                .fillMaxWidth(), // Row가 화면 전체를 차지하게
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ManagerStudentInfo("학번", "20221313", modifier = Modifier.weight(0.7f))
            ManagerStudentInfo("학년", "3", modifier = Modifier.weight(0.3f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        ManagerStudentInfo("학과", "소프트웨어융합", modifier = Modifier.padding(horizontal = 20.dp))
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

@Preview(showBackground = true)
@Composable
fun AccessionPreview(){
    AccessionScreen()
    AccessionDetailScreen()
}