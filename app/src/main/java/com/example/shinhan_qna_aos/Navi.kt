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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shinhan_qna_aos.API.APIInterface
//import com.example.shinhan_qna_aos.etc.WriteRepository
//import com.example.shinhan_qna_aos.info.InfoRepository
//import com.example.shinhan_qna_aos.info.InformationScreen
//import com.example.shinhan_qna_aos.info.WaitScreen
import com.example.shinhan_qna_aos.login.api.AuthRepository
import com.example.shinhan_qna_aos.login.api.LoginResult
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.api.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginScreen
//import com.example.shinhan_qna_aos.main.MainScreen
//import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation(
    navController : NavHostController = rememberNavController(),
    apiInterface: APIInterface
) {
    val context = LocalContext.current

    // 데이터 및 리포지토리 초기화
    val loginManager = remember { Data(context) }
    val onboardingRepository = remember { OnboardingRepository(context) }
    val authRepository = remember { AuthRepository(apiInterface, loginManager) }
//    val writeRepository = remember { WriteRepository(apiInterface, loginManager) }
//    val postRepository = remember { PostRepository(apiInterface, loginManager) }
//    val infoRepository = remember { InfoRepository(apiInterface) }

// 뷰모델 초기화
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = SimpleViewModelFactory { OnboardingViewModel(onboardingRepository) })
    val loginViewModel: LoginViewModel = viewModel(factory = SimpleViewModelFactory { LoginViewModel(authRepository, loginManager) })

    val loginResult by loginViewModel.loginResult.collectAsState()
    val showOnboarding by onboardingViewModel.showOnboarding.collectAsState()

// 앱 첫 진입 시 라우팅 목적지 결정용 상태
    var initialRoute by remember { mutableStateOf<String?>(null) }

// 앱 시작 또는 상태 변화 시 분기 처리
    LaunchedEffect(showOnboarding, loginResult) {
        if (showOnboarding == true) {
            initialRoute = "onboarding"
            return@LaunchedEffect
        }

        if (loginResult is LoginResult.Success) {
            if (loginManager.isAdmin) {
                initialRoute = "main"
                return@LaunchedEffect
            }

            val accessToken = loginManager.accessToken
            if (accessToken.isNullOrEmpty()) {
                initialRoute = "login"
                return@LaunchedEffect
            }

            val route = withContext(Dispatchers.IO) {
                try {
                    val response = apiInterface.UserCheck("Bearer $accessToken")
                    if (response.isSuccessful) {
                        val user = response.body()
                        when (user?.status) {
                            "가입 완료" -> "main"
                            "가입 대기 중" -> "wait/${user.name}"
                            null -> "wait/${user?.name}"
                            else -> "info"
                        }
                    } else {
                        "info"
                    }
                } catch (e: Exception) {
                    "info"
                }
            }
            initialRoute = route
            return@LaunchedEffect
        }

        initialRoute = "login"
    }

    if (initialRoute == null) {
        return
    }

    NavHost(
        navController = navController,
        startDestination = initialRoute!!
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onboardingRepository,
                onFinish = {
                    onboardingViewModel.setOnboarded()
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(authRepository, loginManager, navController)
        }

        composable("manager login") {
            ManagerLoginScreen(authRepository, loginManager, navController)
        }

//        composable("info") {
//            InformationScreen(infoRepository, loginManager)
//        }
//
//        composable("wait/{userName}") { backStackEntry ->
//            val userName = backStackEntry.arguments?.getString("userName") ?: "학생"
//            WaitScreen(userName = userName)
//        }
//
//        composable("main") {
//            MainScreen(postRepository, loginManager, navController)
//        }
//        // 학생용 상세 게시글
//        composable(
//            "postDetail/{postId}",
//            arguments = listOf(navArgument("postId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
//            WriteOpenScreen(navController, postRepository,postId, loginManager)
//        }
//        // 관리자용 상세 게시글
//        composable(
//            "managerPostDetail/{postId}",
//            arguments = listOf(navArgument("postId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
//            ManagerWriteOpenScreen(isNotice = false, postRepository = postRepository , loginManager =loginManager , navController = navController, postId = postId)
//        }
//        composable("writeboard"){
//            WritingScreen(writeRepository,navController)
//        }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
