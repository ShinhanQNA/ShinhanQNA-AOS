package com.example.shinhan_qna_aos.main.api

import com.google.gson.annotations.SerializedName

// API에서 받은 데이터 모델
data class Post(
    @SerializedName("postId") val postID: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("date") val date: String,
    @SerializedName("imagePath") val imagePath: String?,
    @SerializedName("category") val category: String,
    @SerializedName("status") val status: String,
    @SerializedName("email") val email: String?,
    @SerializedName("year") val year: String?
)
// UI용 데이터 모델 (responseState 필드 추가)
data class TitleContentLike(
    val postID: Int,
    val title: String,
    val content: String,
    val likeCount: Int,
    val flagsCount: Int = 0, // 관리자 api 로 받아와야함
    val banCount: Int = 0,  // 관리자 api 로 받아와야함
    val responseState: String = "응답 상태" // 관리자 api 로 받아와야함
)

data class PostLike(
    val likes: Int
)