package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.login.LoginBackendResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface APIInterface {

    @POST("/api/auth/kakao-login")
    fun kakaoAccessToken(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): Call<LoginBackendResponse>
}
