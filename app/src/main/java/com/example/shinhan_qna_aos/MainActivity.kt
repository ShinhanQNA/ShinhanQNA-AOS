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
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.sendKakaoAuthCodeToServer
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import com.example.shinhan_qna_aos.ui.theme.Shinhan_QNA_AOSTheme
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

        // http://서버주소/oauth/callback/kakao?code=O_8-FePDEWQ 이런 형태에서 code만 추출
        intent?.data?.let { uri ->
            val code = uri.getQueryParameter("code")
            Log.d(TAG, "받은 인가코드만: $code") // 이 라인이 logcat에 출력됨
            if (code != null) {
                sendKakaoAuthCodeToServer(code, TAG)
            }
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
