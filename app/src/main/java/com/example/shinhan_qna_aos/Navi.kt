package com.example.shinhan_qna_aos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.etc.WriteOpenScreen
import com.example.shinhan_qna_aos.etc.WritingScreen
import com.example.shinhan_qna_aos.etc.api.WriteRepository
import com.example.shinhan_qna_aos.info.api.InfoRepository
import com.example.shinhan_qna_aos.info.InformationScreen
import com.example.shinhan_qna_aos.info.WaitScreen
import com.example.shinhan_qna_aos.login.api.AuthRepository
import com.example.shinhan_qna_aos.login.api.LoginResult
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.api.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginScreen
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

@Composable
fun AppNavigation(
    navController : NavHostController = rememberNavController(),
    apiInterface: APIInterface
) {
    val context = LocalContext.current

    // 데이터 및 리포지토리 초기화
    val data = remember { Data(context) }
    val onboardingRepository = remember { OnboardingRepository(context) }
    val authRepository = remember { AuthRepository(apiInterface, data) }
    val writeRepository = remember { WriteRepository(apiInterface, data) }
    val postRepository = remember { PostRepository(apiInterface, data) }
    val infoRepository = remember { InfoRepository(apiInterface) }

// 뷰모델 초기화
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = SimpleViewModelFactory { OnboardingViewModel(onboardingRepository) })
    val loginViewModel: LoginViewModel = viewModel(factory = SimpleViewModelFactory { LoginViewModel(authRepository, data) })

    val showOnboarding by onboardingViewModel.showOnboarding.collectAsState()
    val loginResult by loginViewModel.loginResult.collectAsState()

    // 앱 첫 진입 시 라우팅 목적지 결정용 상태
    var initialRoute by remember { mutableStateOf<String?>(null) }
    // 앱 시작 또는 상태 변화 시 분기 처리
    LaunchedEffect(showOnboarding, loginResult) {
        if (showOnboarding == true) {
            initialRoute = "onboarding"
            return@LaunchedEffect
        }
        if (loginResult is LoginResult.Success) {
            if (data.isAdmin) {
                initialRoute = "main"
                return@LaunchedEffect
            }
            val accessToken = data.accessToken
            if (accessToken.isNullOrEmpty()) {
                initialRoute = "login"
                return@LaunchedEffect
            }
            // 최초 가입요청(정보 입력)한 적 없는 경우 → info로 이동
            val didSubmitInfo = data.userInfoSubmitted // (예: sharedPrefs/DB 저장)
            if (didSubmitInfo) {
                // 이미 가입요청한 경우에는 서버에 UserCheck 요청해서 state에 따라 화면 분기
                val userStatus = data.userStatus
                initialRoute = when (userStatus) {
                    "가입 완료" -> "main"
                    "가입 대기 중" -> "wait"
                    else -> "info"
                }
            }
            else {
                initialRoute = "info"
            }
            return@LaunchedEffect
        }
        initialRoute = "login"
    }
    if (initialRoute == null) return


    NavHost(
        navController = navController,
        startDestination = initialRoute!!
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    onboardingViewModel.setOnboarded()
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("login") { LoginScreen(authRepository, data, navController) }

        composable("manager_login") { ManagerLoginScreen(authRepository, navController, data) }

        composable("info") { InformationScreen(infoRepository, data,navController) }

        composable("wait") { WaitScreen(infoRepository, data, navController) }

        composable("main") { MainScreen(postRepository, data, navController) }

        /**
         *  게시글 관련 API
         */
        composable( // postID를 같이 넘기는
            "postDetail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            WriteOpenScreen(navController, postRepository,postId, data)
        }

        composable("writeBoard"){
            WritingScreen(writeRepository,navController)
        }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
