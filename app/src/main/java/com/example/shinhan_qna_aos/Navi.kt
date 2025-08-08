package com.example.shinhan_qna_aos

import android.util.Log
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
import com.example.shinhan_qna_aos.main.SaySomtingViewModel
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
    val navigateTo by infoViewModel.navigateTo.collectAsState()
    // 온보딩 상태 체크
    LaunchedEffect(showOnboarding, loginResult) {
        if (showOnboarding == false) {
            when (loginResult) {
                is LoginResult.Success -> {
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
    // InfoViewModel에서 상태 바뀌면 네비게이션
    LaunchedEffect(navigateTo) {
        Log.d("AppNavigation", "navigateTo 상태 변경 감지: $navigateTo")
        if (!navigateTo.isNullOrEmpty()) {
            navController.navigate(navigateTo!!) {
                popUpTo("login") { inclusive = true }
            }
            infoViewModel.resetNavigateTo()
        }
    }


    NavHost(
        navController = navController,
        startDestination =  if (showOnboarding == true) "onboarding" else "login"
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
            val userName = backStackEntry.arguments?.getString("userName") ?: "학생"
            WaitScreen(userName = userName)
        }
        composable("main") { MainScreen() }
    }
}

