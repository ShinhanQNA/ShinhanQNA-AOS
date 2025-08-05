package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.login.LoginBackendResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST("/oauth/callback/kakao")
    suspend fun kakaoOpenIdToken(
        @Header("Authorization") openIdToken: String
    ): Response<LoginBackendResponse>

    @Headers("Content-Type: application/json")
    @POST("/oauth/callback/google") // 백엔드 API 엔드포인트에 맞게 수정
    suspend fun googleAuthCode(
        @Header("Authorization") code: String
    ): Response<LoginBackendResponse>
}
