package com.example.shinhan_qna_aos.login

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.BuildConfig
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.ui.theme.pretendard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val kakaoAuthUrl =
        "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=${BuildConfig.KAKAO_REST_API}" + // REST API 키
                "&redirect_uri=${BuildConfig.LOCAL_URL}/oauth/callback/kakao" + // 딥링크 URI와 일치
                "&response_type=code"

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
            .padding(top = 64.dp, start = 40.dp, end = 40.dp, bottom = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxwidth=maxWidth
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
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoAuthUrl))
                            context.startActivity(intent)
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 구글 로그인 버튼
                Image(
                    painter = painterResource(R.drawable.google_login),
                    contentDescription = "구글 로그인",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
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

fun sendKakaoAuthCodeToServer(code: String, TAG: String) {
    val authCodeHeader = "kakaotcode $code"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = APIRetrofit.apiService.kakaoAuthcode(authCodeHeader)
            if (response.isSuccessful) {
                Log.d(TAG, "서버 인증 성공: ${response.body()}")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.d(TAG, "서버 인증 실패: $errorBody")
            }
        } catch (e: Exception) {
            Log.d(TAG, "네트워크 오류: ${e.message}")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun loginpreview(){
    LoginScreen()
}