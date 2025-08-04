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
    suspend fun kakaoAuthcode(
        @Header("Authorization") code: String
    ): Response<LoginBackendResponse>

}
