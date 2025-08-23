package com.example.shinhan_qna_aos.API

import com.example.shinhan_qna_aos.info.api.UserCheckResponse
import com.example.shinhan_qna_aos.info.api.InfoResponse
import com.example.shinhan_qna_aos.info.api.UserResponseWrapper
import com.example.shinhan_qna_aos.login.api.AdminRequest
import com.example.shinhan_qna_aos.login.api.LoginTokensResponse
import com.example.shinhan_qna_aos.login.api.LogoutData
import com.example.shinhan_qna_aos.login.api.RefreshTokenRequest
import com.example.shinhan_qna_aos.main.api.Answer
import com.example.shinhan_qna_aos.main.api.Post
import com.example.shinhan_qna_aos.main.api.PostData
import com.example.shinhan_qna_aos.main.api.PostDetail
import com.example.shinhan_qna_aos.main.api.PostFlag
import com.example.shinhan_qna_aos.main.api.PostLike
import com.example.shinhan_qna_aos.main.api.ReportReasonBody
import com.example.shinhan_qna_aos.main.api.TWPostData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
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
    ): Response<LoginTokensResponse>

    // 관리자 로그인
    @POST("/admin/login")
    suspend fun AdminLoginData(
        @Body adminRequest : AdminRequest
    ):Response<LoginTokensResponse>

    // 로그아웃
    @POST("/users/logout")
    suspend fun LogOut(
        @Header("Authorization") refreshToken: String,
    ): Response<LogoutData>

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
        @Part("studentCertified") studentCertified : RequestBody,
        @Part image: MultipartBody.Part
    ): Response<InfoResponse>

    //유저 정보 조회
    @Headers("Content-Type: application/json")
    @GET("/users/me")
    suspend fun UserCheck(
        @Header("Authorization") accessToken: String
    ): Response<UserResponseWrapper>

    //게시글 조회
    @Headers("Content-Type: application/json")
    @GET("/boards")
    suspend fun getPosts(
        @Header("Authorization") code : String
    ):Response<List<PostData>>

    //게시글 상세조회
    @Headers("Content-Type: application/json")
    @GET("/boards/{postsid}")
    suspend fun getPostsDetail(
        @Header("Authorization") accessToken: String,
        @Path("postsid") postsid: String?
    ):Response<PostDetail>

    // 게시글 작성
    @Multipart
    @POST("/boards")
    suspend fun uploadPost(
        @Header("Authorization") accessToken: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ):Response<Post>

    // 게시글 수정
    @Multipart
    @PUT("/boards/{postsid}")
    suspend fun updatePost(
        @Header("Authorization") accessToken: String,
        @Path("postsid") postsid: Int,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Post>

    // 게시글 공감
    @POST("/boards/{postId}/like")
    suspend fun PostLike(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,  // postId로 수정 (URL 변수명과 같아야 함)
    ): Response<PostLike>

    // 게시글 공감 취소
    @POST("/boards/{postId}/unlike")
    suspend fun PostUnlike(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,
    ): Response<PostLike>

    // 게시글 신고
    @POST("/boards/{postId}/report")
    suspend fun PostFlag(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,
        @Body reportReasonBody: ReportReasonBody   // 신고 이유 -> 없어도 됨
    ): Response<PostFlag>

    // 게시글 삭제
    @DELETE("/boards/{postsid}")
    suspend fun PostDelete(
        @Header("Authorization") accessToken: String,
        @Path("postsid") postsid: Int,
    ): Response<Unit>

    // 3주 조회
    @GET("/three-week-opinions/group/{groupId}?sort=date|likes")
    suspend fun ThreeWeekPost(
        @Header("Authorization") accessToken: String,
        @Path ("groupId") groupId :Int,
        @Path ("sort")	sort: String	// date OR likes
    ): Response<TWPostData>

    //답변 조회
    @GET("/answers")
    suspend fun AnswerPost(
        @Header("Authorization") accessToken: String,
    ): Response<List<Answer>>
}

