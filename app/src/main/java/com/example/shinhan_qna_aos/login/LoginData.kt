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

// 자동 매핑
data class LoginBackendResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("expires_in") val expiresIn: Int
)

data class ApiErrorResponse(
    val status: Int,
    val message: String
)
