package com.example.shinhan_qna_aos.main

// API에서 받은 데이터 모델 (이미 주신 코드 그대로)
data class Post(
    val PostID: Int,
    val Title: String,
    val Content: String,
    val Likes: Int,
    val Date: String,
    val Image_path: String,
    val Category: String,
    val Status: String
)

// UI용 데이터 모델 (responseState 필드 추가)
data class TitleContentLike(
    val postID: Int,
    val title: String,
    val content: String,
    val likeCount: Int,
    val flagsCount: Int = 0,
    val banCount: Int = 0,
    val responseState: String = "응답 상태"
)

data class PostListResponse(
    val posts: List<Post>,
    val total: Int
)
