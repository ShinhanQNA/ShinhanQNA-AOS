package com.example.shinhan_qna_aos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

class MainActivity : ComponentActivity() {

    val loginRepository = LoginRepository()

    private val loginViewModel: LoginViewModel by viewModels {
        SimpleViewModelFactory { LoginViewModel(loginRepository) }
    }
    private val infoViewModel: InfoViewModel by viewModels {
        SimpleViewModelFactory { InfoViewModel() }
    }
    private val onboardingViewModel: OnboardingViewModel by viewModels {
        SimpleViewModelFactory { OnboardingViewModel() }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(loginViewModel,infoViewModel,onboardingViewModel)
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

// 공통적으로 쓸 수 있는 공용 Factory 패턴
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}