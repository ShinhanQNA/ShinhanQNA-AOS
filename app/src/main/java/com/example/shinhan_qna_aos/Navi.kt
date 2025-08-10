package com.example.shinhan_qna_aos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.info.InformationScreen
import com.example.shinhan_qna_aos.info.WaitScreen
import com.example.shinhan_qna_aos.login.LoginResult
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLogin
import com.example.shinhan_qna_aos.login.ManagerLoginViewModel
import com.example.shinhan_qna_aos.login.LoginManager
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    infoViewModel: InfoViewModel,
    onboardingViewModel: OnboardingViewModel,
    managerLoginViewModel:ManagerLoginViewModel,
    loginmanager: LoginManager
) {
    val navController = rememberNavController()

    val loginResult by loginViewModel.loginResult.collectAsState()
    val showOnboarding by onboardingViewModel.showOnboarding.collectAsState()

    // 처음 진입 시 결정될 시작 경로
    var initialRoute by remember { mutableStateOf<String?>(null) }

    // 앱 첫 진입 시 라우팅 목적지 미리 결정
    LaunchedEffect(showOnboarding, loginResult) {
        when {
            // 온보딩 필요
            showOnboarding == true -> {
                initialRoute = "onboarding"
            }
            // 로그인 성공 → 가입 상태 먼저 체크
            loginResult is LoginResult.Success -> {
                val route = withContext(Dispatchers.IO) {
                    // 가입 상태 바로 조회 (서버 호출, UI 출력 전 경로 결정)
                    val accessToken = loginmanager.accessToken
                    if (accessToken.isNullOrEmpty()) { "login" }
                    else {
                        try {
                            val response = infoViewModel.api.UserCheck("Bearer $accessToken")
                            if (response.isSuccessful) {
                                val user = response.body()
                                when (user?.status) {
                                    "가입 대기 중" -> "wait/${user.name}"
                                    "가입 완료" -> "main"
                                    else -> "info"
                                }
                            } else {
                                "info"
                            }
                        } catch (e: Exception) {
                            "info"
                        }
                    }
                }
                initialRoute = route
            }
            // 로그인 안 됨
            else -> {
                initialRoute = "login"
            }
        }
    }
    if (initialRoute == null) {
        // 빈 화면 또는 로딩 화면
        return
    }


    // NavHost 시작
    NavHost(
        navController = navController,
        startDestination = initialRoute!!
    ) {
        composable("onboarding") {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onFinish = {
                    onboardingViewModel.setOnboarded()
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("login") { LoginScreen(viewModel = loginViewModel, navController)}
        composable("manager login") {
            ManagerLogin(
                viewModel = managerLoginViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("managerLogin") { inclusive = true }
                    }
                }
            )
        }
        composable("info") { InformationScreen(viewModel = infoViewModel) }
        composable("wait/{userName}") {
            val userName = it.arguments?.getString("userName") ?: "학생"
            WaitScreen(userName = userName)
        }
        composable("main") { MainScreen() }
    }
}
