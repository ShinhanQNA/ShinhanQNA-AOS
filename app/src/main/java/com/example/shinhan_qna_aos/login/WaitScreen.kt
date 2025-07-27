package com.example.shinhan_qna_aos.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R

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

        // 조건에 따른 텍스트 스타일 적용
        val titleStyle = if (isCompact) 32.sp else 25.sp
        val descStyle = if (isCompact) 25.sp else 20.sp
        val imageSize = if (isCompact) 60.dp else 100.dp

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.biglogo), contentDescription = null)
            Text(
                text = "가입 요청 완료",
                fontSize = titleStyle
                )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "[사용자 이름]님의 가입 신청 내용을 안전하게 전달했어요.\n" +
                        "\n" +
                        "관리자 확인까지 잠시 시간이 걸릴 수 있습니다."
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WaitPreview(){
    WaitScreen()
}