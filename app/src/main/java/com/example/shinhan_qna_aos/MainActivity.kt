package com.example.shinhan_qna_aos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val TAG = "kakao"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainEntry() }
        handleKaKaoRedirect(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleKaKaoRedirect(intent)
    }

    private fun handleKaKaoRedirect(intent: Intent?) {
        Log.d(TAG, "handleKaKaoRedirect called, intent = $intent")
        Log.d(TAG, "intent data = ${intent?.data}")

        // 백엔드 서버에서 리다이렉트된 URI에서 'code' 파라미터를 추출
        intent?.data?.let { uri ->
            val code = uri.getQueryParameter("code")
            Log.d(TAG, "받은 인가코드: $code")
            if (code != null) {
                sendKakaoAuthCodeToServer(code, TAG)
            }
        }
    }
}

fun sendKakaoAuthCodeToServer(code: String, TAG: String) {
    val authCodeHeader = "kakaotoken $code"
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
fun MainEntry(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val onboardingviewmodel= OnboardingViewModel()
    // 첫 진입시 온보딩 완료 여부 체크
    LaunchedEffect(Unit) {
        showOnboarding = !OnboardingPrefs.isOnboarded(context)
    }

    if (showOnboarding) {
        OnboardingScreen(
            viewModel = onboardingviewmodel,
            onFinish = {
                scope.launch {
                    OnboardingPrefs.setOnboarded(context, true)
                    showOnboarding = false // 로그인으로 전환
                }
            }
        )
    } else {
        LoginScreen()
    }
}
