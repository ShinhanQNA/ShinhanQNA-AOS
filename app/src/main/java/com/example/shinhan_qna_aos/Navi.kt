package com.example.shinhan_qna_aos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.info.InformationScreen
import com.example.shinhan_qna_aos.info.WaitScreen
import com.example.shinhan_qna_aos.login.LoginResult
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.login.TokenManager
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    infoViewModel: InfoViewModel,
    onboardingViewModel: OnboardingViewModel,
    tokenManager: TokenManager
) {

    val navController = rememberNavController()
    val loginResult by loginViewModel.loginResult.collectAsState()
    val showOnboarding by onboardingViewModel.showOnboarding.collectAsState()
    // 가입 대기 상태 관찰 (Flow 또는 StateFlow 로 관리하면 더 좋음)
    val isWaitingApproval = remember { mutableStateOf(tokenManager.isUserWaitingForApproval) }

    // 온보딩 상태 체크
    LaunchedEffect(showOnboarding, loginResult, isWaitingApproval.value) {
        if (showOnboarding == false) {
            when {
                // 로그인 성공 & 대기중 상태면 wait 화면으로
                loginResult is LoginResult.Success && isWaitingApproval.value -> {
                    navController.navigate("wait/${infoViewModel.state.name}") {
                        popUpTo(0)
                    }
                }
                loginResult is LoginResult.Success -> {
                    navController.navigate("info") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = if (showOnboarding == true) "onboarding" else "login"
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
        composable("login") { LoginScreen(viewModel = loginViewModel) }
        composable("info") { InformationScreen(viewModel = infoViewModel,navController) }
        // 네비게이션 그래프에 WaitScreen 경로 정의 (예시)
        composable("wait/{userName}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "사용자"
            WaitScreen(userName = userName)
        }
        composable("main") { MainScreen() }
    }
}

