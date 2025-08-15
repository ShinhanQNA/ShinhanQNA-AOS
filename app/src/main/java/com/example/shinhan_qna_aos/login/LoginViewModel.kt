package com.example.shinhan_qna_aos.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    loginManager: LoginManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> get() = _loginResult

    init {
        val access = loginManager.accessToken
        if (!access.isNullOrBlank() && !loginManager.isAccessTokenExpired()) {
            _loginResult.value = LoginResult.Success(
                accessToken = access,
                refreshToken = loginManager.refreshToken.orEmpty(),
                expiresIn = ((loginManager.accessTokenExpiresAt - System.currentTimeMillis()) / 1000).toInt()
            )
        }
        tryRefreshTokenIfNeeded()
    }

    // 카카오 로그인
    fun loginWithKakao(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                _loginResult.value = LoginResult.Failure(-1, error.localizedMessage ?: "로그인 실패")
            } else if (token != null) {
                viewModelScope.launch {
                    repository.loginWithKakao(token.accessToken.trim())
                        .onSuccess {
                            _loginResult.value = LoginResult.Success(it.accessToken, it.refreshToken, it.expiresIn)
                        }
                        .onFailure { e ->
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

    // 구글 로그인
    fun sendGoogleAuthCodeToServer(authCode: String) {
        viewModelScope.launch {
            repository.loginWithGoogle(authCode)
                .onSuccess {
                    _loginResult.value = LoginResult.Success(it.accessToken, it.refreshToken, it.expiresIn)
                }
                .onFailure { e ->
                    _loginResult.value = LoginResult.Failure(-1, e.localizedMessage ?: "구글 로그인 실패")
                }
        }
    }

    // 토큰 재발급 시도
    fun tryRefreshTokenIfNeeded() {
        viewModelScope.launch {
            if (!repository.refreshTokenIfNeeded()) {
                _loginResult.value = LoginResult.Failure(-1, "재로그인 필요")
                // 필요시 로그아웃 처리로직 추가 가능
            }
        }
    }
}
