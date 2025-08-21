package com.example.shinhan_qna_aos.main.api

import com.google.gson.annotations.SerializedName

// API에서 받은 데이터 모델 (전체 조회)
data class PostData(
    @SerializedName("postId") val postID: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String,
    @SerializedName("reportCount") val reportCount: Int,
    @SerializedName("warningStatus") val warningStatus: String // 값이 경고면 경고, 차단이면 밴
)
// UI용 데이터 모델 (responseState 필드 추가)
data class TitleContentLike(
    val postID: Int,
    val title: String,
    val content: String,
    val likeCount: Int,
    val flagsCount: Int = 0, // 관리자 api 로 받아와야함
    val banCount: String,  // 관리자 api 로 받아와야함
    val responseState: String = "응답 상태" // 관리자 api 로 받아와야함
)

data class PostLike(
    val likes: Int
)

data class PostFlag(
    val reportId: Int,
    val postId: Int,
    val reporterEmail: String,
    val reportReason: String,
    val reportDate: String,
    val resolved: Boolean
)

// 신고 사유를 Body로 보내는 데이터 클래스
data class ReportReasonBody(
    val reportReason: String?
)

// API에서 받은 데이터 모델 게시글 수정/작성
data class Post(
    @SerializedName("postId") val postID: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("date") val date: String,
    @SerializedName("imagePath") val imagePath: String?,
    @SerializedName("status") val status: String,
    @SerializedName("email") val email: String,
    @SerializedName("year") val year: String?
)

// API에서 받은 데이터 모델 게시글 (상세 조회)
data class PostDetail(
    @SerializedName("postId") val postID: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String,
    @SerializedName("reportCount") val reportCount: Int,
    @SerializedName("warningStatus") val warningStatus: String,
    @SerializedName("writerEmail") val writerEmail: String,
    @SerializedName("imagePath") val imagePath: String?,

)
