package com.example.shinhan_qna_aos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.etc.WriteRepository
import com.example.shinhan_qna_aos.etc.WritingViewModel
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginViewModel
import com.example.shinhan_qna_aos.login.LoginManager
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.onboarding.OnboardingRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

class MainActivity : ComponentActivity() {

    private lateinit var loginmanager: LoginManager
    private lateinit var onboardingRepository: OnboardingRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var onboardingViewModel: OnboardingViewModel
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var managerLoginViewModel: ManagerLoginViewModel
    private lateinit var writingViewModel: WritingViewModel
    private lateinit var postViewModel: PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 의존성 먼저 생성
        loginmanager = LoginManager(applicationContext)
        onboardingRepository = OnboardingRepository(applicationContext)
        val apiInterface = APIRetrofit.apiService
        val postRepository = PostRepository(apiInterface,loginmanager)
        val writeRepository = WriteRepository(apiInterface,loginmanager)
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

        postViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { PostViewModel(postRepository,loginmanager) }
        )[PostViewModel::class.java]

        writingViewModel = ViewModelProvider(
            this,
            SimpleViewModelFactory { WritingViewModel(writeRepository) }
        )[WritingViewModel::class.java]

        // 4. Compose 시작
        setContent {
            AppNavigation(
                loginViewModel = loginViewModel,
                onboardingViewModel = onboardingViewModel,
                infoViewModel = infoViewModel,
                managerLoginViewModel = managerLoginViewModel,
                postViewModel = postViewModel,
                writingViewModel = writingViewModel,
                loginmanager = loginmanager,
            )
        }
    }
}
// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
