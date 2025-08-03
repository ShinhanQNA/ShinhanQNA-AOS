package com.example.shinhan_qna_aos.login

import com.google.gson.annotations.SerializedName

data class LoginData(
    val name: String = "",
    val studentId: String = "",
    val grade: String? = "",
    val major: String? = ""
)

data class ManagerLoginData(
    val managerId: String = "",
    val managerPassword: String = ""
)

// 서버 로그인 성공 시 응답 데이터 (access_token, refresh_token, expires_in)
data class LoginBackendResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

// 서버에서 로그인 실패 시 반환하는 에러 데이터
data class ApiErrorResponse(
    val status: Int,
    val message: String
)
