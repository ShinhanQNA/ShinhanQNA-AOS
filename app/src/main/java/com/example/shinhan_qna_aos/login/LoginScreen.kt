package com.example.shinhan_qna_aos.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.ui.theme.pretendard

@Composable
fun LoginScreen(viewModel: LoginViewModel,navController:NavController) {
    val context = LocalContext.current
    val loginResult by viewModel.loginResult.collectAsState()
    // 로그인 결과 표시 (토큰 수신 성공/실패)

    LaunchedEffect(loginResult) {
        Log.d("LoginScreen", "==== loginResult 변경됨: $loginResult ====")
        when (loginResult) {
            is LoginResult.Success -> {
                Log.d(
                    "LoginScreen", "로그인 성공! 액세스 토큰: ${(loginResult as LoginResult.Success).accessToken} " +
                            "리프레시 토큰: ${(loginResult as LoginResult.Success).refreshToken} " +
                            "만료 시간: ${(loginResult as LoginResult.Success).expiresIn} 초"
                )
                navController.navigate("main") {
                    // 로그인 화면을 백스택에서 제거하여 뒤로가기 시 다시 로그인 화면으로 안 돌아가게 처리
                    popUpTo("login") { inclusive = true }
                }
            }
            is LoginResult.Failure -> {
                Log.e("LoginScreen", "로그인 실패: ${(loginResult as LoginResult.Failure).errorMsg}")
            }
            else -> {
                Log.d("LoginScreen", "Idle 또는 기타 상태")
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
            .padding(top = 64.dp, start = 40.dp, end = 40.dp, bottom = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxw = maxWidth
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 카카오 로그인 버튼
                Image(
                    painter = painterResource(R.drawable.kakao_login),
                    contentDescription = "카카오 로그인",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openKakaoLogin(context) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 구글 로그인 버튼
                Image(
                    painter = painterResource(R.drawable.google_login),
                    contentDescription = "구글 로그인",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openGoogleLogin(context) }
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
                    modifier = Modifier.clickable { /* 관리자 로그인 처리 */ }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun loginpreview(){

}