package com.example.shinhan_qna_aos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginViewModel
import com.example.shinhan_qna_aos.login.LoginManager
import com.example.shinhan_qna_aos.onboarding.OnboardingRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

class MainActivity : ComponentActivity() {

    private lateinit var loginmanager: LoginManager
    private lateinit var onboardingRepository: OnboardingRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var managerLoginViewModel: ManagerLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 의존성 먼저 생성
        loginmanager = LoginManager(applicationContext)
        onboardingRepository = OnboardingRepository(applicationContext)
        val apiInterface = APIRetrofit.apiService

        // 2. ViewModel 생성
        loginViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { LoginViewModel(apiInterface, loginmanager) }
        )[LoginViewModel::class.java]

        onboardingViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { OnboardingViewModel(onboardingRepository) }
        )[OnboardingViewModel::class.java]

        infoViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { InfoViewModel(apiInterface, loginmanager) }
        )[InfoViewModel::class.java]

        managerLoginViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { ManagerLoginViewModel(apiInterface, loginmanager) }
        )[ManagerLoginViewModel::class.java]

        // 3. 토큰 유효성 검사 및 자동 갱신 시도
        loginViewModel.tryRefreshTokenIfNeeded()

        // 4. Compose 시작
        setContent {
            AppNavigation(
                loginViewModel = loginViewModel,
                onboardingViewModel = onboardingViewModel,
                infoViewModel = infoViewModel,
                managerLoginViewModel = managerLoginViewModel,
                loginmanager = loginmanager
            )
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
