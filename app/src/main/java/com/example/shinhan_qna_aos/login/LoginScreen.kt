package com.example.shinhan_qna_aos.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.ui.theme.pretendard

@Composable
fun LoginScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // 상태바+내비게이션 영역 침범 방지
            .background(Color.White)
            .padding(top = 64.dp, start = 40.dp, end = 40.dp, bottom = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp



        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.biglogo),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )

            /**관리자 로그인**/
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){// 카카오 로그인 버튼 (화면 너비에 따라 크기 조절)
                Image(
                    painter = painterResource(R.drawable.kakao_login),
                    contentDescription = "카카오 로그인",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
                // 구글 로그인 버튼 (화면 너비에 따라 크기 조절)
                Image(
                    painter = painterResource(R.drawable.google_login),
                    contentDescription = "구글 로그인",
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "관리자 전용 페이지",
                    color = Color(0xffDFDFDF),
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.clickable { /**관리자 로그인**/ }
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun loginpreview(){
    LoginScreen()
}