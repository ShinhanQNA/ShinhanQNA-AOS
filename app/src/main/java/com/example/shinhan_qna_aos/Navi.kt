package com.example.shinhan_qna_aos

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shinhan_qna_aos.login.LoginRepository
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.LoginViewModel
import com.example.shinhan_qna_aos.main.MainScreen

@Composable
fun AppNavigation(viewModel: LoginViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            MainEntry(navController = navController)
        }
        composable("login") {
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable("main") {
            MainScreen()
        }
        // 필요에 따라 추가 화면 composable 등록 가능
    }
}
