package com.example.shinhan_qna_aos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.TokenManager
import com.example.shinhan_qna_aos.onboarding.OnboardingRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

//class MainActivity : ComponentActivity() {
//
//    private val loginRepository = LoginRepository()
//    private val apiInterface: APIInterface = APIRetrofit.apiService
//    private lateinit var tokenManager: TokenManager
//    private lateinit var onboardingRepository: OnboardingRepository
//
//    private val loginViewModel: LoginViewModel by viewModels {
//        SimpleViewModelFactory {
//            LoginViewModel(loginRepository, apiInterface, tokenManager)
//        }
//    }
//
//    private val onboardingViewModel: OnboardingViewModel by viewModels {
//        SimpleViewModelFactory {
//            OnboardingViewModel(onboardingRepository)
//        }
//    }
//
//    private val infoViewModel: InfoViewModel by viewModels {
//        SimpleViewModelFactory {
//            InfoViewModel()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 토큰 매니저 초기화 (applicationContext 꼭 사용)
//        tokenManager = TokenManager(applicationContext)
//
//        // 온보딩 Repository 초기화
//        onboardingRepository = OnboardingRepository(applicationContext)
//
//        // 앱 시작 시 토큰 만료 여부 체크(있으면 재발급 시도)
//        loginViewModel.tryRefreshTokenIfNeeded()
//
//        setContent {
//            AppNavigation(loginViewModel, infoViewModel, onboardingViewModel,tokenManager)
//        }
//
//        // 최초 진입 시 딥링크 체크
//        handleDeepLink(intent)
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        Log.d("MainActivity", "onNewIntent: $intent data=${intent.data}")
//        handleDeepLink(intent)
//    }
//
//    private fun handleDeepLink(intent: Intent) {
//        val data = intent.data
//        Log.d("MainActivity", "handleDeepLink data=$data")
//        if (data != null && data.scheme == BuildConfig.MY_SCHEME && data.host == "login") {
//            val accessToken = data.getQueryParameter("accessToken")
//            val refreshToken = data.getQueryParameter("refreshToken")
//            val expiresIn = data.getQueryParameter("expiresIn")?.toIntOrNull() ?: 0
//
//            Log.d("MainActivity", "Parameters: accessToken=$accessToken, refreshToken=$refreshToken, expiresIn=$expiresIn")
//            loginViewModel.onReceivedTokens(accessToken, refreshToken, expiresIn)
//        }
//    }
//}

class MainActivity : ComponentActivity() {

    private val apiInterface: APIInterface = APIRetrofit.apiService
    private lateinit var tokenManager: TokenManager
    private lateinit var onboardingRepository: OnboardingRepository

    private val loginViewModel: LoginViewModel by viewModels {
        SimpleViewModelFactory {
            LoginViewModel(apiInterface, tokenManager)
        }
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels {
        SimpleViewModelFactory {
            OnboardingViewModel(onboardingRepository)
        }
    }

    private val infoViewModel: InfoViewModel by viewModels {
        SimpleViewModelFactory {
            InfoViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 토큰 저장 초기화
        tokenManager = TokenManager(applicationContext)
        // 온보딩 Repository 초기화
        onboardingRepository = OnboardingRepository(applicationContext)
        // 토큰 자동 갱신 시도 등 초기 작업
        loginViewModel.tryRefreshTokenIfNeeded()

        setContent {
            AppNavigation(loginViewModel = loginViewModel, onboardingViewModel = onboardingViewModel, infoViewModel = infoViewModel, tokenManager = tokenManager)
        }

//        // 최초 진입 인텐트에서 구글 인가코드 처리 (필요시)
//        handleGoogleAuthCode(intent)
    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        // 구글 OAuth 인가코드가 포함된 URI 처리
//        handleGoogleAuthCode(intent)
//    }

//    private fun handleGoogleAuthCode(intent: Intent?) {
//        val data = intent?.data ?: return
//
//        if (data.toString().startsWith("${BuildConfig.BASE_URL}/oauth/callback/google")) {
//            val authCode = data.getQueryParameter("code")
//            if (!authCode.isNullOrBlank()) {
//                loginViewModel.sendGoogleAuthCodeToServer(authCode)
//            }
//        }
//    }
}
// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
