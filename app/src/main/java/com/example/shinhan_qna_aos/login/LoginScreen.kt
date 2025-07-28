package com.example.shinhan_qna_aos.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R

@Composable
fun LoginScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // 상태바+내비게이션 영역 침범 방지
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp

        // 조건에 따른 텍스트 스타일 적용
        val titleStyle = if (isCompact) 32.sp else 25.sp
        val descStyle = if (isCompact) 25.sp else 20.sp
        val imageSize = if (isCompact) 60.dp else 100.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isCompact) 16.dp else 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.biglogo), contentDescription = null, modifier = Modifier.size(imageSize))
            Spacer(modifier = Modifier.height(20.dp))
            // 카카오 로그인 버튼 (화면 너비에 따라 크기 조절)
            Button(
                onClick = {
                /* TODO */
                    // 로그인 인증 관련 코드 필요
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 48.dp else 56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "카카오 로그인",
                    fontSize = descStyle
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            // 구글 로그인 버튼 (화면 너비에 따라 크기 조절)
            Button(
                onClick = {
                    /* TODO */
                    // 로그인 인증 관련 코드 필요
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 48.dp else 56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "구글 로그인",
                    fontSize = descStyle
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "관리자 전용 페이지",
                color = Color.Gray,
                modifier = Modifier.clickable{ /**관리자 로그인**/ }
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun loginpreview(){
    LoginScreen()
}