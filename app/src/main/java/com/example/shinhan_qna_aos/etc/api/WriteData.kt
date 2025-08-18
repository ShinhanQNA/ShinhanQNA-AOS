package com.example.shinhan_qna_aos.etc.api

import android.net.Uri

// 글쓰기 API
data class Write(
    val postId: Int,
    val title: String,
    val content: String,
    val likes: Int,
    val date: String,
    val imagePath: String?,
    val category: String,
    val status: String,
    val email: String,
    val year: String
)

// 글쓰기 ui 용
data class WriteData(
    val title: String,
    val content: String,
    val category: String?,
    val imageUri: Uri? = Uri.EMPTY
)


//data class NotificationWriteData (
//    val title: String? ="",
//    val content: String? =""
//)