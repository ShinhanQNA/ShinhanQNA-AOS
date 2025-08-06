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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.LoginViewModelFactory
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels {
        // 반드시 직접 만든 Factory 넘기기!
        LoginViewModelFactory(LoginRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(loginViewModel)
        }
        // 최초 진입 시 딥링크 체크
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent: $intent data=${intent.data}")
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val data = intent.data
        Log.d("MainActivity", "handleDeepLink data=$data")
        if (data != null && data.scheme == BuildConfig.MY_SCHEME && data.host == "login") {
            val accessToken = data.getQueryParameter("accessToken")
            val refreshToken = data.getQueryParameter("refreshToken")
            val expiresIn = data.getQueryParameter("expiresIn")?.toIntOrNull() ?: 0

            Log.d("MainActivity", "Parameters: accessToken=$accessToken, refreshToken=$refreshToken, expiresIn=$expiresIn")
            loginViewModel.onReceivedTokens(accessToken, refreshToken, expiresIn)
        }
    }
}

    @Composable
fun MainEntry(navController: NavController) {
    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
        val onboardingViewModel: OnboardingViewModel = viewModel()
        // 첫 진입시 온보딩 완료 여부 체크
    LaunchedEffect(Unit) {
        showOnboarding = !OnboardingPrefs.isOnboarded(context)
    }

    if (showOnboarding) {
        OnboardingScreen(
            viewModel = onboardingViewModel,
            onFinish = {
                scope.launch {
                    OnboardingPrefs.setOnboarded(context, true)
                    showOnboarding = false // 로그인으로 전환
                }
            }
        )
    } else {
        navController.navigate("login") {
            // 로그인 화면을 백스택에서 제거하여 뒤로가기 시 다시 로그인 화면으로 안 돌아가게 처리
            popUpTo("login") { inclusive = true }
        }
    }
}
