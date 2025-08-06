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

sealed class LoginResult {
    object Idle : LoginResult() // 로그인 대기중
    data class Success(
        val accessToken: String,
        val refreshToken: String,
        val expires_in: Int
    ) : LoginResult()
    data class Failure(val errorMsg: String) : LoginResult()
}