package com.example.shinhan_qna_aos.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.BuildConfig
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val apiInterface: APIInterface,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult

    // 앱스킴으로 토큰 받음
    fun onReceivedTokens(accessToken: String?, refreshToken: String?, expiresIn: Int) {
        Log.d("LoginViewModel", "onReceivedTokens 호출: accessToken=$accessToken, refreshToken=$refreshToken, expires_in=$expiresIn")
        if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
            // 토큰 저장
            tokenManager.saveTokens(accessToken, refreshToken, expiresIn)
            _loginResult.value = LoginResult.Success(accessToken, refreshToken, expiresIn)
            Log.d("LoginViewModel", "LoginResult.Success 세팅 완료")
        } else {
            _loginResult.value = LoginResult.Failure(500,"Invalid token from deep link")
            Log.e("LoginViewModel", "LoginResult.Failure 세팅 - 토큰값 부적합")
        }
    }

    // 토큰 저장/만료 검사 후 재발급 시도
    fun tryRefreshTokenIfNeeded() {
        viewModelScope.launch {
            val isAccessTokenExpired = tokenManager.isAccessTokenExpired()
            val isRefreshTokenExpired = tokenManager.isRefreshTokenExpired()

            when {
                isAccessTokenExpired && isRefreshTokenExpired -> {
                    tokenManager.clearTokens()
                    _loginResult.value = LoginResult.Failure(
                        status = 500,
                        message = "토큰이 만료되어 재로그인이 필요합니다."
                    )
                }
                isAccessTokenExpired && !isRefreshTokenExpired -> {
                    val refreshToken = tokenManager.refreshToken
                    if (refreshToken.isNullOrBlank()) {
                        _loginResult.value = LoginResult.Failure(
                            status = 400,
                            message = "유효한 Refresh token이 존재하지 않습니다."
                        )
                        return@launch
                    }

                    val response = runCatching { apiInterface.ReToken(refreshToken) }
                        .onFailure { e -> Log.e("LoginViewModel", "ReToken 호출 실패", e) }
                        .getOrNull()

                    if (response == null) {
                        _loginResult.value = LoginResult.Failure(
                            status = 500,
                            message = "통신 오류로 토큰 재발급 실패"
                        )
                        return@launch
                    }

                    if (response.isSuccessful) {
                        val newToken = response.body()
                        if (newToken != null) {
                            tokenManager.accessToken = newToken.accessToken
                            tokenManager.accessTokenExpiresAt = System.currentTimeMillis() + newToken.expiresIn * 1000L
                            _loginResult.value = LoginResult.Success(
                                accessToken = newToken.accessToken,
                                refreshToken = refreshToken,
                                expiresIn = newToken.expiresIn
                            )
                        } else {
                            tokenManager.clearTokens()
                            _loginResult.value = LoginResult.Failure(
                                status = 500,
                                message = "Access token 재발급 실패: 응답 데이터 오류"
                            )
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        tokenManager.clearTokens()
                        _loginResult.value = LoginResult.Failure(
                            status = response.code(),
                            message = errorBody ?: response.message()
                        )
                    }
                }
                isRefreshTokenExpired -> {
                    tokenManager.clearTokens()
                    _loginResult.value = LoginResult.Failure(
                        status = 500,
                        message = "Refresh token 만료, 재로그인이 필요합니다."
                    )
                }
                else -> {
                    val accessToken = tokenManager.accessToken
                    val refreshToken = tokenManager.refreshToken
                    if (accessToken != null && refreshToken != null) {
                        val expiresIn = ((tokenManager.accessTokenExpiresAt - System.currentTimeMillis()) / 1000).toInt()
                        _loginResult.value = LoginResult.Success(accessToken, refreshToken, expiresIn)
                    } else {
                        _loginResult.value = LoginResult.Failure(
                            status = 500,
                            message = "토큰 정보가 유효하지 않습니다."
                        )
                    }
                }
            }
        }
    }

    // 카카오 로그인 URL 오픈
    fun openKakaoLogin(context: Context) {
        val url = repository.getKakaoLoginUrl()
        Log.d("LoginViewModel", "카카오 로그인 URL: $url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    // 구글 로그인 URL 오픈
    fun openGoogleLogin(context: Context) {
        val url = repository.getGoogleLoginUrl()
        Log.d("LoginViewModel", "구글 로그인 URL: $url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}

//class LoginViewModel(
//    private val apiInterface: APIInterface,
//    private val tokenManager: TokenManager
//) : ViewModel() {
//
//    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
//    val loginResult: StateFlow<LoginResult> = _loginResult
//
//    // 카카오 SDK 로그인
//    fun loginWithKakao(context: Context) {
//        val client = UserApiClient.instance
//        if (client.isKakaoTalkLoginAvailable(context)) {
//            client.loginWithKakaoTalk(context) { token, error ->
//                if (error != null) {
//                    _loginResult.value = LoginResult.Failure(-1, "카카오톡 로그인 실패: ${error.message}")
//                } else if (token != null) {
//                    sendKakaoAccessTokenToServer(token.accessToken)
//                }
//            }
//        } else {
//            client.loginWithKakaoAccount(context) { token, error ->
//                if (error != null) {
//                    _loginResult.value = LoginResult.Failure(-1, "카카오 계정 로그인 실패: ${error.message}")
//                } else if (token != null) {
//                    sendKakaoAccessTokenToServer(token.accessToken)
//                }
//            }
//        }
//    }
//
//    // 서버로 카카오 액세스 토큰 전달
//    private fun sendKakaoAccessTokenToServer(accessToken: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiInterface.KakaoAuthCode("Bearer $accessToken")
//                if (response.isSuccessful) {
//                    val result = response.body()
//                    when (result) {
//                        is LoginResult.Success -> {
//                            tokenManager.saveTokens(result.accessToken, result.refreshToken, result.expiresIn)
//                            _loginResult.value = result
//                        }
//                        is LoginResult.Failure -> _loginResult.value = result
//                        else -> _loginResult.value = LoginResult.Failure(-1, "서버 응답 데이터 오류")
//                    }
//                } else {
//                    _loginResult.value = LoginResult.Failure(response.code(), response.errorBody()?.string() ?: response.message())
//                }
//            } catch (e: Exception) {
//                _loginResult.value = LoginResult.Failure(-1, "서버 통신 실패: ${e.message}")
//            }
//        }
//    }
//
//    // 구글 인가코드 받아 서버에 POST 요청
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
//                            Log.d("LoginVM", "구글 서버 로그인 성공")
//                        }
//                        is LoginResult.Failure -> _loginResult.value = result
//                        else -> _loginResult.value = LoginResult.Failure(-1, "알 수 없는 서버 응답")
//                    }
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: response.message()
//                    _loginResult.value = LoginResult.Failure(response.code(), errorMsg)
//                }
//            } catch (e: Exception) {
//                Log.e("LoginVM", "구글 서버 로그인 중 오류", e)
//                _loginResult.value = LoginResult.Failure(-1, "서버 통신 실패")
//            }
//        }
//    }
//
//    // 토큰 재발급 처리
//    fun tryRefreshTokenIfNeeded() {
//        viewModelScope.launch {
//            val refreshToken = tokenManager.refreshToken
//            if (refreshToken.isNullOrBlank()) {
//                _loginResult.value = LoginResult.Failure(-1, "Refresh token 없음")
//                return@launch
//            }
//
//            try {
//                val response = apiInterface.ReToken("Bearer $refreshToken")
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body != null) {
//                        tokenManager.saveTokens(body.accessToken, refreshToken, body.expiresIn)
//                        _loginResult.value = LoginResult.Success(body.accessToken, refreshToken, body.expiresIn)
//                        Log.d("LoginVM", "토큰 재발급 성공")
//                    } else {
//                        _loginResult.value = LoginResult.Failure(-1, "토큰 재발급 응답 없음")
//                    }
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: response.message()
//                    _loginResult.value = LoginResult.Failure(response.code(), errorMsg)
//                }
//            } catch (e: Exception) {
//                Log.e("LoginVM", "토큰 재발급 중 오류", e)
//                _loginResult.value = LoginResult.Failure(-1, "서버 통신 실패")
//            }
//        }
//    }
//}
//
//fun getGoogleLoginUrl(): String {
//    val clientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
//    val redirectUri = "${BuildConfig.BASE_URL}/oauth/callback/google"
//    val scope = "email profile"
//    return "https://accounts.google.com/o/oauth2/v2/auth" +
//            "?client_id=$clientId" +
//            "&redirect_uri=$redirectUri" +
//            "&response_type=code" +  // 인가코드 방식
//            "&scope=$scope" +
//            "&access_type=offline"   // 리프레시 토큰 받으려면 필요
//}
