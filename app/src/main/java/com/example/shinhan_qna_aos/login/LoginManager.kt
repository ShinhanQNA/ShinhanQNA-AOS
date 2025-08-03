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
    private val onLoginResult: (Boolean) -> Unit  // 로그인 성공 여부 콜백
) {
    private val kakaoRepository = KakaoRepository()

    /** 카카오 SDK 로그인 콜백 */
    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> {
                // SDK 로그인 실패
                onLoginResult(false)
            }
            token != null -> {
                // SDK 로그인 성공 → 서버 호출
                CoroutineScope(Dispatchers.IO).launch {
                    val result = kakaoRepository.accessToken(token.accessToken)
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = { resp ->
                                // 서버 로그인 성공 시 토큰 저장 로직 추가
                                onLoginResult(true)
                            },
                            onFailure = {
                                // 서버 로그인 실패
                                onLoginResult(false)
                            }
                        )
                    }
                }
            }
        }
    }

    /** 카카오 로그인 실행 */
    fun loginWithKakao() {
        // 카카오톡 앱이 설치되어 있으면 앱으로 로그인, 아니면 계정(웹) 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }
}