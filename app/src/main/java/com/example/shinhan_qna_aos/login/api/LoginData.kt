package com.example.shinhan_qna_aos.login.api

import com.google.gson.annotations.SerializedName


data class ManagerLoginData(
    val managerId: String = "",
    val managerPassword: String = ""
)

data class AdminRequest(
    val id : String,
    val password : String
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)

data class LoginTokensResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("expires_in") val expiresIn: Int
)

// ViewModel 내부 상태 표현용 sealed class
sealed class LoginResult {
    object Idle : LoginResult()
    data class Success(
        val accessToken: String,
        val refreshToken: String,
        val expiresIn: Int
    ) : LoginResult()
    data class Failure(
        val status: Int,
        val message: String
    ) : LoginResult()
}
