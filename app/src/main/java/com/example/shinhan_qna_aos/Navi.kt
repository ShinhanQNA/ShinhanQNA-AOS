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
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.api.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginScreen
import com.example.shinhan_qna_aos.login.api.LoginResult
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.servepage.MypageScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    apiInterface: APIInterface
) {
    val context = LocalContext.current

    // Data & Repository
    val data = remember { Data(context) }
    val authRepository = remember { AuthRepository(apiInterface, data) }
    val writeRepository = remember { WriteRepository(apiInterface, data) }
    val postRepository = remember { PostRepository(apiInterface, data) }
    val infoRepository = remember { InfoRepository(apiInterface) }
    val loginViewModel: LoginViewModel =
        viewModel(factory = SimpleViewModelFactory { LoginViewModel(authRepository, data) })
    val infoViewModel: InfoViewModel =
        viewModel(factory = SimpleViewModelFactory { InfoViewModel(infoRepository, data) })

    val loginResult by loginViewModel.loginResult.collectAsState()

    // 라우트 변경 감시 → 네비게이션 처리 (여기서만!)
    val navigationRoute by infoViewModel.navigationRoute.collectAsState()

    // 앱 최초 진입 시 빠르게 보여줄 초기 화면 결정용 상태
    var initialRoute by remember { mutableStateOf<String?>(null) }

//    // 앱 첫 진입 시 라우팅 목적지 미리 결정
//    LaunchedEffect(loginResult) {
//        when {
//            data.onboarding -> {
//                initialRoute = "onboarding"
//            }
//            loginResult is LoginResult.Success -> {
//                Log.d("AppNavigation", "Login success detected in AppNavigation")
//
//                // 로그인 성공 시 서버에서 유저 상태 확인 요청
//                infoViewModel.checkAndNavigateUserStatus(data.accessToken ?: "")
//
//                // navigationRoute가 아직 null일 수 있으므로 대기하지 말고 fallback 지정
//                initialRoute = navigationRoute.takeIf { !it.isNullOrBlank() } ?: "login"
//
//                Log.d("AppNavigation", "Initial route set to $initialRoute based on navigationRoute or fallback")
//            }
//            else -> {
//                initialRoute = "login"
//            }
//        }
//    }
//    // 서버에서 받은 navigationRoute 값이 변경되면 네비게이션 실행
//    LaunchedEffect(navigationRoute) {
//        navigationRoute?.let { route ->
//            // 현재 화면(route)와 새로 이동할 경로가 다르고, 빈 문자열이 아닐 때 이동
//            if (navController.currentDestination?.route != route && route.isNotBlank()) {
//                Log.d("AppNavigation", "navigationRoute 변동 감지되어 $route 로 이동")
//                navController.navigate(route) {
//                    // 시작 지점까지 쌓인 화면 히스토리를 전부 제거하여 뒤로가기 시 혼란 방지
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//    }
    // 로그인 결과 바뀔 때 초기화 및 서버 상태 요청
    LaunchedEffect(loginResult) {
        if (data.onboarding) {
            initialRoute = "onboarding"
        } else if (loginResult is LoginResult.Success) {
            Log.d("AppNavigation", "로그인 성공 감지, 서버 상태 조회 시작")
            infoViewModel.checkAndNavigateUserStatus(data.accessToken ?: "")
            // 초기 화면은 navigationRoute가 정해질 때까지 null로 둔다
            initialRoute = null
        } else {
            initialRoute = "login"
        }
    }

    // navigationRoute가 바뀌면 초기 라우트로 고정
    LaunchedEffect(navigationRoute) {
        navigationRoute?.let {
            if (initialRoute != it && it.isNotBlank()) {
                Log.d("AppNavigation", "navigationRoute 확정: $it")
                initialRoute = it
            }
        }
    }
    // 초기 라우트가 null 이면 NavHost 렌더링 안 함 (startDestination에 빈값 전달 방지)
    if (initialRoute.isNullOrBlank()) return

    NavHost(
        navController = navController,
        startDestination = initialRoute!!
    ) {
        composable("onboarding") { OnboardingScreen(navController, data) }
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

        composable("writeBoard") { WritingScreen(writeRepository, navController) }
        composable("myPage") { MypageScreen(authRepository, data, navController) }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
