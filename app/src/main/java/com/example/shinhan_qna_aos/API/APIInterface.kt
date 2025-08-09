package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.info.InfoData
import com.example.shinhan_qna_aos.info.UserCheckResponse
import com.example.shinhan_qna_aos.login.AdminRequest
import com.example.shinhan_qna_aos.login.LoginResult
import com.example.shinhan_qna_aos.login.LoginTokensResponse
import com.example.shinhan_qna_aos.login.ManagerLoginData
import com.example.shinhan_qna_aos.login.ReToken
import com.example.shinhan_qna_aos.login.RefreshTokenRequest
import com.example.shinhan_qna_aos.main.PostListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface APIInterface {
    //kakao
    @Headers("Content-Type: application/json")
    @POST("/oauth/callback/kakao")
    suspend fun KakaoAuthCode(
        @Header("Authorization") accessToken: String
    ): Response<LoginTokensResponse>

    //구글
    @Headers("Content-Type: application/json")
    @POST("/oauth/callback/google")
    suspend fun GoogleAuthCode(
        @Header("Authorization") code : String
    ): Response<LoginTokensResponse>

    // 토큰 재발급
    @POST("/token/reissue")
    suspend fun ReToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<ReToken>

    // 관리자 로그인
    @POST("/admin/login")
    suspend fun AdminLoginData(
        @Body adminRequest : AdminRequest
    ):Response<LoginTokensResponse>

    // 학생 정보
    @Multipart
    @POST("/users/certify")
    suspend fun InfoStudent(
        @Header("Authorization") accessToken: String,
        @Part("students") students: RequestBody,
        @Part("name") name: RequestBody,
        @Part("department") department: RequestBody,
        @Part("year") year: RequestBody,
        @Part("role") role: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<InfoData>

    //유저 정보 조회
    @Headers("Content-Type: application/json")
    @GET("/users/me")
    suspend fun UserCheck(
        @Header("Authorization") accessToken: String
    ): Response<UserCheckResponse>

    //게시글 조회
    @Headers("Content-Type: application/json")
    @GET("/boards/search")
    suspend fun getPosts(
        @Header("Authorization") code : String,
        @Query("size") size: Int,                        // 아이템 요청 개수
        @Query("sort") sort: String
    ):Response<PostListResponse>
}

