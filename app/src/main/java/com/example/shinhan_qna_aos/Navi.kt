package com.example.shinhan_qna_aos

import android.util.Log
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
import com.example.shinhan_qna_aos.info.api.InfoViewModel
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
import com.example.shinhan_qna_aos.servepage.MypageScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    apiInterface: APIInterface
) {
    val context = LocalContext.current

    // 각종 데이터 및 리포지토리 초기화
    val data = remember { Data(context) }
    val onboardingRepository = remember { OnboardingRepository(context) }
    val authRepository = remember { AuthRepository(apiInterface, data) }
    val writeRepository = remember { WriteRepository(apiInterface, data) }
    val postRepository = remember { PostRepository(apiInterface, data) }
    val infoRepository = remember { InfoRepository(apiInterface) }

    // 뷰모델 초기화
    val onboardingViewModel: OnboardingViewModel = viewModel(factory = SimpleViewModelFactory { OnboardingViewModel(onboardingRepository) })
    val loginViewModel: LoginViewModel = viewModel(factory = SimpleViewModelFactory { LoginViewModel(authRepository, data) })
    val infoViewModel: InfoViewModel = viewModel(factory = SimpleViewModelFactory { InfoViewModel(infoRepository, data) })

    val showOnboarding by onboardingViewModel.showOnboarding.collectAsState()
    val loginResult by loginViewModel.loginResult.collectAsState()

    // 앱 첫 진입시 이동할 목적지를 결정하기 위한 상태
    val navigationRoute by infoViewModel.navigationRoute.collectAsState()

    LaunchedEffect(showOnboarding, loginResult) {
        if (showOnboarding != null) {  // null 체크 추가
            infoViewModel.determineInitialRoute(showOnboarding!!, loginResult)
        }
    }

    LaunchedEffect(navigationRoute) {
        navigationRoute?.let { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // navigationRoute가 null인 경우는 화면을 그리지 않음
    if (navigationRoute == null) return


    NavHost(
        navController = navController,
        startDestination = navigationRoute!!
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

        composable("login") { LoginScreen(authRepository, infoRepository, data, navController) }

        composable("manager_login") { ManagerLoginScreen(authRepository, navController, data) }

        composable("info") { InformationScreen(infoRepository, data, navController) }

        composable("wait") { WaitScreen(infoRepository, data, navController) }

        composable("main") { MainScreen(postRepository, data, navController) }

        composable(
            "writeOpen/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            WriteOpenScreen(navController, postRepository, writeRepository, data, postId)
        }

        composable("writeBoard") {
            WritingScreen(writeRepository, navController)
        }

        composable("myPage") {
            MypageScreen(authRepository, data, navController)
        }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
