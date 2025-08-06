package com.example.shinhan_qna_aos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainEntry()
        }
        // 최초 진입 시 딥링크 체크
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 이미 실행중인 Activity에 딥링크 오면 호출됨
        intent?.let { handleDeepLink(it) }
    }

    private fun handleDeepLink(intent: Intent) {
        Log.d("MainActivity", "handleDeepLink called with: $intent")
        intent.data?.let { uri ->
            Log.d("MainActivity", "딥링크 URI 수신: $uri")
            if (uri.scheme == BuildConfig.MY_SCHEME && uri.host == "login") {
                val accessToken = uri.getQueryParameter("accessToken")
                val refreshToken = uri.getQueryParameter("refreshToken")
                val expiresInStr = uri.getQueryParameter("expires_in")
                val type = uri.getQueryParameter("type")  // 추가로 구분용 파라미터 삽입 가능 (예: kakao, google)

                Log.d("MainActivity", "쿼리 파라미터 - accessToken=$accessToken, refreshToken=$refreshToken, expires_in=$expiresInStr, type=$type")

                val expiresIn = expiresInStr?.toIntOrNull() ?: 0

                loginViewModel.onReceivedTokens(accessToken, refreshToken, expiresIn)
            } else {
                Log.w("MainActivity", "딥링크 URI 스킴/호스트 불일치: scheme=${uri.scheme}, host=${uri.host}")
            }
        }
    }
}

    @Composable
fun MainEntry(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = LoginRepository()
    val viewModel = LoginViewModel(repository)
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
        LoginScreen(viewModel)
    }
}
