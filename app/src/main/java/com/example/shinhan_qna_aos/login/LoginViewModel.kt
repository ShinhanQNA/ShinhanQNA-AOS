package com.example.shinhan_qna_aos.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit.apiService
import com.example.shinhan_qna_aos.BuildConfig
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiInterface: APIInterface,
    private val loginmanager: LoginManager
): ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> get() = _loginResult

    init {
        // 앱 시작 시 저장된 토큰이 아직 유효하면 곧바로 Success로 설정
        val access = loginmanager.accessToken
        val refresh = loginmanager.refreshToken
        if (!access.isNullOrBlank() && !loginmanager.isAccessTokenExpired()) {
            _loginResult.value = LoginResult.Success(
                accessToken = access,
                refreshToken = refresh.orEmpty(),
                expiresIn = ((loginmanager.accessTokenExpiresAt - System.currentTimeMillis()) / 1000).toInt()
            )
        }
    }

    // 카카오 로그인 처리
    fun loginWithKakao(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                _loginResult.value = LoginResult.Failure(-1, error.localizedMessage ?: "로그인 실패")
            } else if (token != null) {
                // 서버에 토큰 전송하여 인증 처리
                viewModelScope.launch {
                    try {
                        val response = apiService.KakaoAuthCode(token.accessToken.trim())
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                loginmanager.saveTokens(body.accessToken, body.refreshToken, body.expiresIn)
                                _loginResult.value = LoginResult.Success(
                                    accessToken = body.accessToken,
                                    refreshToken = body.refreshToken,
                                    expiresIn = body.expiresIn
                                )
                                Log.d("LoginViewModel", "${body.accessToken} ${body.refreshToken} ${body.expiresIn}")
                            } else {
                                _loginResult.value = LoginResult.Failure(response.code(), "응답 데이터 없음")
                            }
                        } else {
                            _loginResult.value = LoginResult.Failure(response.code(), response.message())
                        }
                    } catch (e: Exception) {
                        _loginResult.value = LoginResult.Failure(-1, e.localizedMessage ?: "서버 통신 실패")
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    // 구글 인가 코드 서버 전송
//    fun sendGoogleAuthCodeToServer(authCode: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiInterface.GoogleAuthCode("Bearer $authCode")
//                if (response.isSuccessful) {
//                    val result = response.body()
//                    when (result) {
//                        is LoginResult.Success -> {
//                            tokenManager.saveTokens(result.accessToken, result.refreshToken, result.expiresIn)
//                            _loginResult.value = result
//                        }
//                        is LoginResult.Failure -> _loginResult.value = result
//                        else -> _loginResult.value = LoginResult.Failure(-1, "알 수 없는 서버 응답")
//                    }
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: response.message()
//                    _loginResult.value = LoginResult.Failure(response.code(), errorMsg)
//                }
//            } catch (e: Exception) {
//                _loginResult.value = LoginResult.Failure(-1, "서버 통신 실패")
//            }
//        }
//    }

    // 리프레시 토큰으로 액세스 토큰 재발급
    fun tryRefreshTokenIfNeeded() {
        viewModelScope.launch {
            Log.d("LoginViewModel", "토큰 재발급 검사 시작")

            val currentAccess = loginmanager.accessToken
            val currentRefresh = loginmanager.refreshToken
            Log.d("LoginViewModel", "현재 저장된 access=$currentAccess refresh=$currentRefresh")

            if (!loginmanager.isAccessTokenExpired()) {
                Log.d("LoginViewModel", "Access Token 아직 유효 → 재발급 안 함")
                return@launch
            }

            val refreshToken = currentRefresh?.trim()
            if (refreshToken.isNullOrBlank() || loginmanager.isRefreshTokenExpired()) {
                Log.w("LoginViewModel", "Refresh Token 만료 혹은 없음 → 재로그인 필요")
                return@launch
            }

            try {
                Log.d("LoginViewModel", "재발급 요청 시작, 사용중인 Refresh Token=$refreshToken")
                val response = apiInterface.ReToken(RefreshTokenRequest(refreshToken))
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("LoginViewModel", "재발급 응답 body=$body")

                    if (body != null) {
                        loginmanager.saveTokens(
                            body.accessToken,
                            refreshToken, // 재발급 시엔 원래 refreshToken 그대로 유지
                            body.expiresIn
                        )
                    }
                } else {
                    Log.e("LoginViewModel", "재발급 실패 코드=${response.code()} 메세지=${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "재발급 중 예외 발생", e)
            }
        }
    }
}

fun getGoogleLoginUrl(): String {
    val clientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
    val redirectUri = "${BuildConfig.BASE_URL}/oauth/callback/google"
    val scope = "email profile"
    return "https://accounts.google.com/o/oauth2/v2/auth" +
            "?client_id=$clientId" +
            "&redirect_uri=$redirectUri" +
            "&response_type=code" +  // 인가코드 방식
            "&scope=$scope" +
            "&access_type=offline"   // 리프레시 토큰 받으려면 필요
}
