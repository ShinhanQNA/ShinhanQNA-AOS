package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.login.LoginBackendResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    // 카카오 로그인: Authorization에 "kakaotoken {토큰}" 형식으로 전송
    @Headers("Content-Type: application/json")
    @POST("/api/auth/kakao-login")
    suspend fun kakaoAccessToken(
        @Header("Authorization") token: String
    ): Response<LoginBackendResponse>

    // 구글 로그인: Authorization에 "googletoken {idToken}" 형식으로 전송
    @Headers("Content-Type: application/json")
    @POST("/api/auth/google-login")
    suspend fun googleLogin(
        @Header("Authorization") token: String
    ): Response<LoginBackendResponse>
}
