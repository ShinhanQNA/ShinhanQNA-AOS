package com.example.shinhan_qna_aos.login

import android.content.Context


class TokenManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("ACCESS_TOKEN", null)
        set(value) = prefs.edit().putString("ACCESS_TOKEN", value).apply()

    var refreshToken: String?
        get() = prefs.getString("REFRESH_TOKEN", null)
        set(value) = prefs.edit().putString("REFRESH_TOKEN", value).apply()

    var accessTokenExpiresAt: Long
        get() = prefs.getLong("ACCESS_TOKEN_EXP", 0L)
        set(value) = prefs.edit().putLong("ACCESS_TOKEN_EXP", value).apply()

    var refreshTokenExpiresAt: Long
        get() = prefs.getLong("REFRESH_TOKEN_EXP", 0L)
        set(value) = prefs.edit().putLong("REFRESH_TOKEN_EXP", value).apply()

    fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.accessTokenExpiresAt = System.currentTimeMillis() + expiresIn * 1000L
        // 예: 리프레시 토큰 만료기간은 30일 후로 가정
        this.refreshTokenExpiresAt = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
    }

    fun isAccessTokenExpired(): Boolean = System.currentTimeMillis() >= accessTokenExpiresAt
    fun isRefreshTokenExpired(): Boolean = System.currentTimeMillis() >= refreshTokenExpiresAt

    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}


