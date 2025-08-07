package com.example.shinhan_qna_aos.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
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
                // case 1: accessToken, refreshToken 모두 만료 → 재로그인 유도
                isAccessTokenExpired && isRefreshTokenExpired -> {
                    tokenManager.clearTokens()
                    _loginResult.value = LoginResult.Failure(
                        status = 500,
                        message = "토큰이 만료되어 재로그인이 필요합니다."
                    )
                }
                // case 2: accessToken 만료, refreshToken 유효 → accessToken 재발급 시도
                isAccessTokenExpired && !isRefreshTokenExpired -> {
                    val refreshToken = tokenManager.refreshToken
                    if (refreshToken != null) {
                        val response = runCatching { apiInterface.ReToken(refreshToken) }.getOrNull()

                        if (response != null) {
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
                            } else if (response.code() == 500) {
                                // 500 에러: 로그인 실패 메시지 처리
                                val errorBody = response.errorBody()?.string()
                                tokenManager.clearTokens()
                                _loginResult.value = LoginResult.Failure(
                                    status = 500,
                                    message = errorBody ?: "로그인 실패"
                                )
                            } else {
                                tokenManager.clearTokens()
                                _loginResult.value = LoginResult.Failure(
                                    status = response.code(),
                                    message = response.message()
                                )
                            }
                        } else {
                            _loginResult.value = LoginResult.Failure(
                                status = 500,
                                message = "통신 오류로 토큰 재발급 실패"
                            )
                        }
                    } else {
                        _loginResult.value = LoginResult.Failure(
                            status = 500,
                            message = "Refresh token이 존재하지 않음"
                        )
                    }
                }
                // case 3: accessToken 유효, refreshToken 만료 → 재로그인 처리 (refresh token 재발급 API 없으면 재로그인)
                isRefreshTokenExpired -> {
                    tokenManager.clearTokens()
                    _loginResult.value = LoginResult.Failure(
                        status = 500,
                        message = "Refresh token 만료, 재로그인이 필요합니다."
                    )
                }
                // case 4: accessToken, refreshToken 모두 유효 → 로그인 성공 상태 유지
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