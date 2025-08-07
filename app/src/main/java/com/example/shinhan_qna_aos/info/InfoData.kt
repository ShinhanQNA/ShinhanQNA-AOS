package com.example.shinhan_qna_aos.info

import android.net.Uri

data class InfoData(
    val message:String
)

data class LoginData(
    val studentId: String,
    val name: String,
    val grade: String?,
    val major: String?,
    val role: String,
    val imageUri: Uri? // 이미지 Uri 저장
)