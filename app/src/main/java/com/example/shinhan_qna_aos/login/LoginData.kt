package com.example.shinhan_qna_aos.login

import com.google.gson.annotations.SerializedName


data class ManagerLoginData(
    val managerId: String = "",
    val managerPassword: String = ""
)

data class ReToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)


sealed class LoginResult {
    object Idle : LoginResult() // 로그인 대기중
    data class Success(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("refresh_token") val refreshToken: String,
        @SerializedName("expires_in") val expiresIn: Int
    ) : LoginResult()
    data class Failure(
        val status: Int,
        val message: String
    ) : LoginResult()
}