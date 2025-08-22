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
    var initialRoute by remember { mutableStateOf<String?>("") }

    // 앱 첫 진입 시 라우팅 목적지 미리 결정
    LaunchedEffect(loginResult) {
        when {
            data.onboarding -> {
                initialRoute = "onboarding"
            }
            loginResult is LoginResult.Success -> {
                Log.d("AppNavigation", "Login success detected in AppNavigation")

                // 로그인 성공 시 서버에서 유저 상태 확인 요청
                infoViewModel.checkAndNavigateUserStatus(data.accessToken ?: "")

                // navigationRoute가 아직 null일 수 있으므로 대기하지 말고 fallback 지정
                initialRoute = navigationRoute.takeIf { !it.isNullOrBlank() } ?: "login"

                Log.d("AppNavigation", "Initial route set to $initialRoute based on navigationRoute or fallback")
            }
            else -> {
                initialRoute = "login"
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
