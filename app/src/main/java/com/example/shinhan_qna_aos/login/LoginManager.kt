package com.example.shinhan_qna_aos.login

import android.content.Context
import android.util.Log

class LoginManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    var accessToken: String?
        get() {
            val value = prefs.getString("ACCESS_TOKEN", null)
            Log.d("LoginManager", "accessToken GET: $value")
            return value
        }
        set(value) {
            Log.d("LoginManager", "accessToken SET: $value")
            prefs.edit().putString("ACCESS_TOKEN", value).apply()
        }

    var refreshToken: String?
        get() {
            val value = prefs.getString("REFRESH_TOKEN", null)
            Log.d("LoginManager", "refreshToken GET: $value")
            return value
        }
        set(value) {
            Log.d("LoginManager", "refreshToken SET: $value")
            prefs.edit().putString("REFRESH_TOKEN", value).apply()
        }

    var accessTokenExpiresAt: Long
        get() = prefs.getLong("ACCESS_TOKEN_EXP", 0L)
        set(value) = prefs.edit().putLong("ACCESS_TOKEN_EXP", value).apply()

    var refreshTokenExpiresAt: Long
        get() = prefs.getLong("REFRESH_TOKEN_EXP", 0L)
        set(value) = prefs.edit().putLong("REFRESH_TOKEN_EXP", value).apply()

    fun saveTokens(
        accessToken: String,
        refreshToken: String,
        expiresIn: Int,
        refreshTokenExpiresIn: Long? = null
    ) {
        val now = System.currentTimeMillis()
        Log.d(
            "LoginManager",
            "[saveTokens] 저장 직전 access=$accessToken refresh=$refreshToken expiresIn=$expiresIn"
        )
        this.accessToken = accessToken.trim()
        this.refreshToken = refreshToken.trim()
        this.accessTokenExpiresAt = now + expiresIn * 1000L

        this.refreshTokenExpiresAt = refreshTokenExpiresIn?.let {
            now + it * 1000L
        } ?: (now + 7L * 24 * 60 * 60 * 1000)
        Log.d(
            "LoginManager",
            "[saveTokens] 저장 완료 accessTokenExpiresAt=$accessTokenExpiresAt refreshTokenExpiresAt=$refreshTokenExpiresAt"
        )
    }

    fun isAccessTokenExpired(): Boolean =
        System.currentTimeMillis() >= accessTokenExpiresAt

    fun isRefreshTokenExpired(): Boolean =
        System.currentTimeMillis() >= refreshTokenExpiresAt

    // 관리자 정보 관련
    fun saveIsAdmin(isAdmin: Boolean) {
        prefs.edit().putBoolean("is_admin", isAdmin).apply()
    }

    fun isAdmin(): Boolean {
        return prefs.getBoolean("is_admin", false)
    }

    // 이메일 저장
    fun setUserEmail(email: String) {
        prefs.edit().putString("user_email", email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }
}

