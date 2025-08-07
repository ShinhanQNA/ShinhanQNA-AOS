package com.example.shinhan_qna_aos.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult

    // 앱스킴으로 토큰 받음
    fun onReceivedTokens(accessToken: String?, refreshToken: String?, expiresIn: Int) {
        Log.d("LoginViewModel", "onReceivedTokens 호출: accessToken=$accessToken, refreshToken=$refreshToken, expires_in=$expiresIn")
        if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
            _loginResult.value = LoginResult.Success(accessToken, refreshToken, expiresIn)
            Log.d("LoginViewModel", "LoginResult.Success 세팅 완료")
        } else {
            _loginResult.value = LoginResult.Failure("Invalid token from deep link")
            Log.e("LoginViewModel", "LoginResult.Failure 세팅 - 토큰값 부적합")
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