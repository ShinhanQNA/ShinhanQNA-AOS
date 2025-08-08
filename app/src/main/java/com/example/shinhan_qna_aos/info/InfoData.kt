package com.example.shinhan_qna_aos.info

import android.net.Uri

data class LoginData(
    val students: Int = 0,        // 학번
    val name: String = "",         // 이름
    val department: String = "",   // 학과
    val year: Int = 0,             // 학년
    val role: String = "사용자",     // 역할
    val imageUri: Uri = Uri.EMPTY  // 이미지 Uri
)