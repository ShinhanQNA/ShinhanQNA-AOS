package com.example.shinhan_qna_aos

import android.content.Context

class Data(private val context: Context) {
    private val prefs = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    companion object Companion {
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val KEY_ACCESS_TOKEN_EXP = "ACCESS_TOKEN_EXP"
        private const val KEY_REFRESH_TOKEN_EXP = "REFRESH_TOKEN_EXP"
        private const val KEY_IS_ADMIN = "IS_ADMIN"
        private const val KEY_USER_STATUS = "USER_STATUS"
        private const val KEY_USER_NAME = "USER_NAME"
        private const val KEY_USER_INFO_SUBMITTED = "USER_INFO_SUBMITTED"
    }

    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_REFRESH_TOKEN, value).apply()

    var accessTokenExpiresAt: Long
        get() = prefs.getLong(KEY_ACCESS_TOKEN_EXP, 0L)
        set(value) = prefs.edit().putLong(KEY_ACCESS_TOKEN_EXP, value).apply()

    var refreshTokenExpiresAt: Long
        get() = prefs.getLong(KEY_REFRESH_TOKEN_EXP, 0L)
        set(value) = prefs.edit().putLong(KEY_REFRESH_TOKEN_EXP, value).apply()

    var isAdmin: Boolean
        get() = prefs.getBoolean(KEY_IS_ADMIN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_ADMIN, value).apply()

    var userStatus: String? // 유저 상태 저장
        get() = prefs.getString(KEY_USER_STATUS, null)
        set(value) = prefs.edit().putString(KEY_USER_STATUS, value).apply()

    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var userInfoSubmitted: Boolean  // 정보요청 처음 하는건지 파악
        get() = prefs.getBoolean(KEY_USER_INFO_SUBMITTED, false)
        set(value) = prefs.edit().putBoolean(KEY_USER_INFO_SUBMITTED, value).apply()


    /**
     * 안전하게 토큰 저장: 입력 토큰 trim 처리 후 저장
     * @param expiresInSeconds - access token 유효시간(초)
     * @param refreshTokenExpiresInSeconds - refresh token 유효시간(초), 없으면 7일 기본 값
     */
    fun saveTokens(
        accessToken: String,
        refreshToken: String,
        expiresInSeconds: Int,
        refreshTokenExpiresInSeconds: Long? = null
    ) {
        val now = System.currentTimeMillis()

        val trimmedAccessToken = accessToken.trim()
        val trimmedRefreshToken = refreshToken.trim()

        this.accessToken = trimmedAccessToken
        this.refreshToken = trimmedRefreshToken

        this.accessTokenExpiresAt = now + expiresInSeconds * 1000L
        this.refreshTokenExpiresAt = refreshTokenExpiresInSeconds?.let {
            now + it * 1000L
        } ?: (now + 7L * 24 * 60 * 60 * 1000) // 기본 7일
    }

    // 액세스 토큰 만료 여부 체크
    fun isAccessTokenExpired(): Boolean = System.currentTimeMillis() >= accessTokenExpiresAt

    // 리프레시 토큰 만료 여부 체크
    fun isRefreshTokenExpired(): Boolean = System.currentTimeMillis() >= refreshTokenExpiresAt

//    // 이메일 저장
//    fun getUserEmail(): String? {
//        return prefs.getString("user_email", null)
//    }
}

