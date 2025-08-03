package com.example.shinhan_qna_aos.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginManager(
    private val context: Context,
    private val onLoginResult: (Boolean) -> Unit // 로그인 성공 여부 콜백
) {
    private val authRepository = AuthRepository()

    // 카카오 SDK 로그인 콜백
    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> onLoginResult(false)  // SDK 로그인 실패
            token != null -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = authRepository.loginWithKakao(token.accessToken)
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = { onLoginResult(true) },
                            onFailure = { onLoginResult(false) }
                        )
                    }
                }
            }
        }
    }

    // 카카오 로그인 실행: 카카오톡 앱 이용 가능 시 앱 로그인, 아니면 웹 계정 로그인
    fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }
}