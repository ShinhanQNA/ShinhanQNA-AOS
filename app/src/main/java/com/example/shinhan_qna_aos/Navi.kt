package com.example.shinhan_qna_aos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shinhan_qna_aos.info.InfoViewModel
import com.example.shinhan_qna_aos.info.InformationScreen
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    infoViewModel: InfoViewModel,
    onboardingViewModel: OnboardingViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            val context = LocalContext.current
            var showOnboarding by remember { mutableStateOf(true) }
            val scope = rememberCoroutineScope()

            // 최초 진입 시 온보딩 체크
            LaunchedEffect(Unit) {
                showOnboarding = !OnboardingPrefs.isOnboarded(context)
                // 온보딩 필요 없으면 바로 login으로 네비게이션
                if (!showOnboarding) {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }

            if (showOnboarding) {
                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onFinish = {
                        scope.launch {
                            OnboardingPrefs.setOnboarded(context, true)
                            // 온보딩 끝나면 로그인 이동
                            navController.navigate("login") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
        composable("login") {
            LoginScreen(viewModel = loginViewModel, navController = navController)
        }
        composable("info") {
            InformationScreen( viewModel = infoViewModel)
        }
        composable("main") {
            MainScreen()
        }
        // 필요에 따라 추가 화면 composable 등록 가능
    }
}
