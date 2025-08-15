package com.example.shinhan_qna_aos.main.api

import com.google.gson.annotations.SerializedName

//// API에서 받은 데이터 모델 (이미 주신 코드 그대로)
//data class Post(
//    @SerializedName("postId") val postID: Int,
//    @SerializedName("title") val title: String,
//    @SerializedName("content") val content: String,
//    @SerializedName("likes") val likes: Int,
//    @SerializedName("date") val date: String,
//    @SerializedName("imagePath") val imagePath: String,
//    @SerializedName("category") val category: String,
//    @SerializedName("status") val status: String,
//    @SerializedName("email") val email: String?,
//    @SerializedName("year") val year: String?
//)
//// UI용 데이터 모델 (responseState 필드 추가)
//data class TitleContentLike(
//    val postID: Int,
//    val title: String,
//    val content: String,
//    val likeCount: Int,
//    val flagsCount: Int = 0,
//    val banCount: Int = 0,
//    val responseState: String = "응답 상태"
//)
//
//data class PostDetail(
//    val postId: Int,
//    val title: String,
//    val content: String,
//    val likes: Int,
//    val date: String,
//    val imagePath: String?,
//    val category: String,
//    val status: String,
//    val email: String, // 작성자 Email
//    val year: String
//)