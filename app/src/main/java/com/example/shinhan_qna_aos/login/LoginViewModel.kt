package com.example.shinhan_qna_aos.login

import android.content.Context
import android.content.Intent
import android.net.Uri
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
    private val tokenManager: TokenManager
): ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> get() = _loginResult

    // 카카오 로그인 처리
    fun loginWithKakao(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                _loginResult.value = LoginResult.Failure(-1, error.localizedMessage ?: "로그인 실패")
            } else if (token != null) {
                // 서버에 토큰 전송하여 인증 처리
                viewModelScope.launch {
                    try {
                        val response = apiService.KakaoAuthCode(" ${token.accessToken}")
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
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
            val refreshToken = tokenManager.refreshToken
            if (refreshToken.isNullOrBlank()) {
                _loginResult.value = LoginResult.Failure(-1, "Refresh token 없음")
                return@launch
            }
            try {
                val response = apiInterface.ReToken("Bearer $refreshToken")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        tokenManager.saveTokens(body.accessToken, refreshToken, body.expiresIn)
                        _loginResult.value = LoginResult.Success(body.accessToken, refreshToken, body.expiresIn)
                    } else {
                        _loginResult.value = LoginResult.Failure(-1, "토큰 재발급 응답 없음")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    _loginResult.value = LoginResult.Failure(response.code(), errorMsg)
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Failure(-1, "서버 통신 실패")
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
