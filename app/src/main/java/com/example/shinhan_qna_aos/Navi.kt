package com.example.shinhan_qna_aos

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.shinhan_qna_aos.etc.user.AppealScreen1
import com.example.shinhan_qna_aos.etc.user.AppealScreen2
import com.example.shinhan_qna_aos.etc.user.AppealScreen3
import com.example.shinhan_qna_aos.info.api.InfoRepository
import com.example.shinhan_qna_aos.info.InformationScreen
import com.example.shinhan_qna_aos.info.WaitScreen
import com.example.shinhan_qna_aos.info.api.InfoViewModel
import com.example.shinhan_qna_aos.login.api.AuthRepository
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.login.api.LoginViewModel
import com.example.shinhan_qna_aos.login.ManagerLoginScreen
import com.example.shinhan_qna_aos.login.api.LoginResult
import com.example.shinhan_qna_aos.main.AnsweredOpenScreen
import com.example.shinhan_qna_aos.main.AnsweredScreen
import com.example.shinhan_qna_aos.main.MainScreen
import com.example.shinhan_qna_aos.main.SelectedDetailScreen
import com.example.shinhan_qna_aos.main.SelectedOpenScreen
import com.example.shinhan_qna_aos.main.api.AnswerRepository
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.TWPostRepository
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.servepage.AlarmScreen
import com.example.shinhan_qna_aos.servepage.MypageScreen
import com.example.shinhan_qna_aos.servepage.NotificationOpenScreen
import com.example.shinhan_qna_aos.servepage.NotificationScreen
import com.example.shinhan_qna_aos.servepage.api.NotificationRepository

@RequiresApi(Build.VERSION_CODES.O)
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
    val answerRepository = remember { AnswerRepository(apiInterface,data) }
    val twPostRepository= remember { TWPostRepository(apiInterface, data) }
    val notificationRepository = remember { NotificationRepository(apiInterface, data) }

    val loginViewModel: LoginViewModel =
        viewModel(factory = SimpleViewModelFactory { LoginViewModel(authRepository, data) })
    val infoViewModel: InfoViewModel =
        viewModel(factory = SimpleViewModelFactory { InfoViewModel(infoRepository, data) })

    val loginResult by loginViewModel.loginResult.collectAsState()

    // 라우트 변경 감시 → 네비게이션 처리 (여기서만!)
    val navigationRoute by infoViewModel.navigationRoute.collectAsState()

    // 앱 최초 진입 시 빠르게 보여줄 초기 화면 결정용 상태
    var initialRoute by remember { mutableStateOf<String?>(null) }

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
        composable("login") { LoginScreen(authRepository, data, navController) }
        composable("manager_login") { ManagerLoginScreen(authRepository, navController, data) }
        composable("info") { InformationScreen(infoRepository, data, navController) }
        composable("wait") { WaitScreen(infoRepository, data, navController) }

        composable(
            "main?selectedTab={selectedTab}",
            arguments = listOf(navArgument("selectedTab") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { backStackEntry ->
            val selectedTab = backStackEntry.arguments?.getInt("selectedTab") ?: 0
            MainScreen(
                postRepository = postRepository,
                answerRepository = answerRepository,
                twPostRepository = twPostRepository,
                authRepository = authRepository,
                infoRepository = infoRepository,
                data = data,
                navController = navController,
                initialSelectedIndex = selectedTab
            )
        }
        composable(
            "writeOpen/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            WriteOpenScreen(navController, postRepository, writeRepository, data, postId)
        }

        composable("writeBoard") { WritingScreen(writeRepository, navController) }
        composable("answer") { AnsweredScreen(answerRepository, navController) }

        composable(
            "answerOpen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            AnsweredOpenScreen(answerRepository, navController, id,)
        }

        composable(
            "threeWeekOpen/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
            SelectedOpenScreen(groupId, twPostRepository, navController)
        }

        composable(
            "threeWeekDetail/{groupId}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            SelectedDetailScreen(groupId , twPostRepository, navController, id)
        }

        composable("myPage") { MypageScreen(authRepository, data, navController) }
        composable("notices") { NotificationScreen(data, notificationRepository, navController) }
        composable("alarm") { AlarmScreen(navController) }

        composable(
            "notices/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            NotificationOpenScreen(id, notificationRepository, navController)
        }

        composable("appeal1"){ AppealScreen1(data, navController) }
        composable("appeal2"){ AppealScreen2(data, navController) }
        composable("appeal3"){ AppealScreen3(infoRepository, data, navController) }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
