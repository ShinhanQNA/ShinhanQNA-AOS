package com.example.shinhan_qna_aos.login

import com.example.shinhan_qna_aos.BuildConfig

class LoginRepository {
    fun getKakaoLoginUrl(): String {
        val clientId = BuildConfig.KAKAO_REST_API
        val redirectUri = "${BuildConfig.BASE_URL}/oauth/callback/kakao"
        return "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code&state=android"
    }

    fun getGoogleLoginUrl(): String {
        val clientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        val redirectUri = "${BuildConfig.BASE_URL}/oauth/callback/google"
        val scope = "email profile"
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=$clientId&redirect_uri=$redirectUri&response_type=code&scope=$scope"
    }
}