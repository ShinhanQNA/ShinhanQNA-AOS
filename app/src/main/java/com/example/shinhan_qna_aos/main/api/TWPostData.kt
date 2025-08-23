package com.example.shinhan_qna_aos.main.api

data class TWPostData(
    val id : Int,
    val selectedYear: Int,
    val selectedMonth: Int,
    val responseStatus: String,
    val createdAt:String,
    val opinions: List<GroupList>
)

data class GroupList(
    val id : Int,
    val postId: Int,
    val title: String,
    val content: String,
    val likes: Int,
    val date: String,
    val imagePath: String?,
    val createdAt: String
)