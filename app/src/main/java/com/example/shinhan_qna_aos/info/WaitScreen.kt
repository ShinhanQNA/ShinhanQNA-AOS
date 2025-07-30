package com.example.shinhan_qna_aos.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.ui.theme.pretendard

// 대기 화면
@Composable
fun WaitScreen (){
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // 상태바+내비게이션 영역 침범 방지
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.biglogo),
                contentDescription = null,
                modifier=Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "가입 요청 완료",
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                lineHeight = 28.sp
                )
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "[사용자 이름]님의 가입 신청 내용을 안전하게 전달했어요.",
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "관리자 확인까지 잠시 시간이 걸릴 수 있습니다.",
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WaitPreview(){
    WaitScreen()
}