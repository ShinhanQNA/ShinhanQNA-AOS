package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.info.InfoData
import com.example.shinhan_qna_aos.login.ReToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIInterface {
    // 토큰 재발급
    @Headers("Content-Type: application/json")
    @POST("/token/reissue")
    suspend fun ReToken(
        @Header("Refresh-Token") refreshToken: String
    ): Response<ReToken>

    // 학생 정보
    @Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("/users/certify")
    suspend fun InfoStudent(
        @Header("Authorization") accessToken: String,
        @Part("studentid") studentId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("department") department: RequestBody,
        @Part("year") year: RequestBody,
        @Part("role") role: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<InfoData>
}
