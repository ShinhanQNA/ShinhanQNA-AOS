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
import com.example.shinhan_qna_aos.etc.user.MyWriteScreen
import com.example.shinhan_qna_aos.servepage.WriteOpenScreen
import com.example.shinhan_qna_aos.servepage.WritingScreen
import com.example.shinhan_qna_aos.servepage.api.WriteRepository
import com.example.shinhan_qna_aos.servepage.user.AppealScreen1
import com.example.shinhan_qna_aos.servepage.user.AppealScreen2
import com.example.shinhan_qna_aos.servepage.user.AppealScreen3
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
import com.example.shinhan_qna_aos.servepage.user.MypageScreen
import com.example.shinhan_qna_aos.servepage.NotificationOpenScreen
import com.example.shinhan_qna_aos.servepage.NotificationScreen
import com.example.shinhan_qna_aos.servepage.api.AppealRepository
import com.example.shinhan_qna_aos.servepage.api.NotificationRepository
import com.example.shinhan_qna_aos.servepage.manager.DeclarationOpenScreen
import com.example.shinhan_qna_aos.servepage.manager.DeclarationScreen
import com.example.shinhan_qna_aos.servepage.manager.ManagerScreen
import com.example.shinhan_qna_aos.servepage.manager.NotificationWriteScreen
import com.example.shinhan_qna_aos.servepage.manager.api.DeclarationRepository

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
    val infoRepository = remember { InfoRepository(apiInterface, data) }
    val answerRepository = remember { AnswerRepository(apiInterface,data) }
    val twPostRepository= remember { TWPostRepository(apiInterface, data) }
    val notificationRepository = remember { NotificationRepository(apiInterface, data) }
    val declarationRepository = remember { DeclarationRepository(data, apiInterface) }
    val appealRepository = remember { AppealRepository(apiInterface, data) }

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
            if (data.isAdmin){
                initialRoute= "main"
            }
            else {
                Log.d("AppNavigation", "로그인 성공 감지, 서버 상태 조회 시작")
                infoViewModel.checkAndNavigateUserStatus()
                // 초기 화면은 navigationRoute가 정해질 때까지 null로 둔다
                initialRoute = null
            }
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
        composable("onboarding") { OnboardingScreen(navController, data) }  // 온보딩
        composable("login") { LoginScreen(authRepository, data, navController) } //로그인
        composable("manager_login") { ManagerLoginScreen(authRepository, navController, data) } // 관리자 로그인 화면
        composable("info") { InformationScreen(infoRepository, data, navController) } // 학생 정보 입력 화면
        composable("wait") { WaitScreen(infoRepository, data, navController) } // 가입 대기 화면

        composable( // 메인 화면 선택 사항이 많아서 selectedTab으로 원하는 화면으로 조정 가능
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
        composable( // 게시글 상세 화면
            "writeOpen/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            WriteOpenScreen(navController, postRepository, writeRepository, authRepository, data, postId)
        }

        composable("writeBoard") { WritingScreen(writeRepository,answerRepository ,navController, data) } // 게시글 작성 화면
        composable("answer") { AnsweredScreen(answerRepository, navController) } // 답변 화면

        composable( // 답변 상세 화면
            "answerOpen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            AnsweredOpenScreen(answerRepository, navController,data, id)
        }

        composable( // 3주차 게시판 리스트로 있음
            "threeWeekOpen/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
            SelectedOpenScreen(groupId, twPostRepository, navController)
        }

        composable( // 3주차 게시판 상세화면
            "threeWeekDetail/{groupId}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: -1
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            SelectedDetailScreen(groupId , twPostRepository, navController, id)
        }

        composable("myPage") { MypageScreen(authRepository, data, navController) } // 마이페이지
        composable("manager_myPage"){ ManagerScreen(navController, authRepository, data) } // 관리자 마이페이지

        composable("my_page_write") { MyWriteScreen(postRepository, navController) } // 내가 작성한 게시글

        composable("notices") { NotificationScreen(data, notificationRepository, navController) } // 공지 화면
        composable( // 공지 상세 화면
            "notices/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            NotificationOpenScreen(id,data ,notificationRepository, navController)
        }
        composable("notices_write"){ NotificationWriteScreen(notificationRepository, navController) } // 관리자 공지 작성 화면

        composable("alarm") { AlarmScreen(navController) } // 알림 화면 나중에 firebase

        composable("appeal1"){ AppealScreen1(appealRepository, infoRepository, data, navController) } // 차단 당했을 경우 사용자 제한 화면으로 appeal3까지 세트
        composable("appeal2"){ AppealScreen2(appealRepository, data, navController) }
        composable("appeal3"){ AppealScreen3(infoRepository, data, navController) }

        composable("declaration") { DeclarationScreen(declarationRepository,postRepository,data,navController) } // 신고된 게시글
        composable("declaration/{postId}" ,arguments = listOf(navArgument("postId") { type = NavType.StringType }) // 신고된 게시글 상세
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            DeclarationOpenScreen(postId,navController, postRepository)
        }
    }
}

// 공통 ViewModelFactory 구현
class SimpleViewModelFactory<T: ViewModel>(
    private val creator: () -> T
): ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}
