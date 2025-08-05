package com.example.shinhan_qna_aos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.login.ApiErrorResponse
import com.example.shinhan_qna_aos.login.LoginScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingPrefs
import com.example.shinhan_qna_aos.onboarding.OnboardingScreen
import com.example.shinhan_qna_aos.onboarding.OnboardingViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val TAG = "kakao"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainEntry() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }
}

fun sendKakaoOpenIdTokenToServer(idToken: String, TAG: String) {
    val idTokenHeader = "kakaoopenid $idToken"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = APIRetrofit.apiService.kakaoOpenIdToken(idTokenHeader)
            when (response.code()) {
                200 -> {
                    val loginResponse = response.body()
                    Log.d(TAG, "서버 인증 성공: $loginResponse")
                    // TODO: 토큰 정보를 저장하고 메인 화면으로 이동
                }
                401 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorResponse = Gson().fromJson(errorBodyString, ApiErrorResponse::class.java)
                        Log.d(TAG, "서버 인증 실패 (401): ${errorResponse.message}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 인증 실패 (401), JSON 파싱 오류: $errorBodyString")
                    }
                }
                500 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorJson = Gson().fromJson(errorBodyString, Map::class.java)
                        Log.d(TAG, "서버 오류 (500): ${errorJson["error"]}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 오류 (500), JSON 파싱 오류: $errorBodyString")
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.d(TAG, "알 수 없는 응답 코드: ${response.code()}, 오류: $errorBody")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "네트워크 오류: ${e.message}")
        }
    }
}

fun sendGoogleAuthCodeToServer(code: String, TAG: String) {
    val authCodeHeader = "googletcode $code"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = APIRetrofit.apiService.googleAuthCode(authCodeHeader)
            when (response.code()) {
                200 -> {
                    val loginResponse = response.body()
                    Log.d(TAG, "서버 인증 성공: $loginResponse")
// TODO: 토큰 정보를 저장하고 메인 화면으로 이동
                }
                401 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorResponse =
                            Gson().fromJson(errorBodyString, ApiErrorResponse::class.java)
                        Log.d(TAG, "서버 인증 실패 (401): ${errorResponse.message}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 인증 실패 (401), JSON 파싱 오류: $errorBodyString")
                    }
                }
                500 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
// 500 에러의 JSON 형식이 다르므로, 그에 맞는 데이터 클래스를 사용해야 함
                        val errorJson = Gson().fromJson(errorBodyString, Map::class.java)
                        Log.d(TAG, "서버 오류 (500): ${errorJson["error"]}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 오류 (500), JSON 파싱 오류: $errorBodyString")
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.d(TAG, "알 수 없는 응답 코드: ${response.code()}, 오류: $errorBody")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "네트워크 오류: ${e.message}")
        }
    }
}

@Composable
fun MainEntry(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val onboardingviewmodel= OnboardingViewModel()
    // 첫 진입시 온보딩 완료 여부 체크
    LaunchedEffect(Unit) {
        showOnboarding = !OnboardingPrefs.isOnboarded(context)
    }

    if (showOnboarding) {
        OnboardingScreen(
            viewModel = onboardingviewmodel,
            onFinish = {
                scope.launch {
                    OnboardingPrefs.setOnboarded(context, true)
                    showOnboarding = false // 로그인으로 전환
                }
            }
        )
    } else {
        LoginScreen()
    }
}
