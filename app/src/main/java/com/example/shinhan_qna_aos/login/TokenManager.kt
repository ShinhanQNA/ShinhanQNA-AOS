package com.example.shinhan_qna_aos.login

import android.content.Context
import android.util.Log

class TokenManager(private val context: Context) {

    // SharedPreferences를 사용하여 토큰과 만료 정보를 저장/읽기 위한 인스턴스 생성
    private val prefs = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    // 액세스 토큰을 가져오거나 저장하는 프로퍼티
    var accessToken: String?
        get() = prefs.getString("access_token", null)    // 저장된 액세스 토큰 값 반환, 없으면 null
        set(value) = prefs.edit().putString("access_token", value).apply()  // 액세스 토큰 저장

    // 리프레시 토큰을 가져오거나 저장하는 프로퍼티
    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)   // 저장된 리프레시 토큰 값 반환, 없으면 null
        set(value) = prefs.edit().putString("refresh_token", value).apply()  // 리프레시 토큰 저장

    // 액세스 토큰 만료 시각 (밀리초 단위, epoch time)
    var accessTokenExpiresAt: Long
        get() = prefs.getLong("access_token_expires_at", 0L)   // 만료 시각이 없으면 0 반환 (만료된 것으로 간주)
        set(value) = prefs.edit().putLong("access_token_expires_at", value).apply()   // 만료 시각 저장

    // 리프레시 토큰 만료 시각 (밀리초 단위, epoch time)
    var refreshTokenExpiresAt: Long
        get() = prefs.getLong("refresh_token_expires_at", 0L)  // 없으면 0 반환
        set(value) = prefs.edit().putLong("refresh_token_expires_at", value).apply()  // 만료 시각 저장

    // 현재 시간이 액세스 토큰 만료 시각을 지났는지 체크
    fun isAccessTokenExpired(): Boolean = System.currentTimeMillis() > accessTokenExpiresAt

    // 현재 시간이 리프레시 토큰 만료 시각을 지났는지 체크
    fun isRefreshTokenExpired(): Boolean = System.currentTimeMillis() > refreshTokenExpiresAt

    /**
     * 새로운 토큰들을 저장하고, 만료 시각도 함께 설정
     *
     * @param accessToken 새로 발급받은 액세스 토큰
     * @param refreshToken 새로 발급받은 리프레시 토큰
     * @param expiresInSeconds 액세스 토큰 만료까지 남은 시간(초 단위)
     *
     * 보통 액세스 토큰 만료시간은 서버가 응답에서 주므로, 이를 기준으로 밀리초 단위로 저장.
     * 리프레시 토큰 만료시간은 서버에서 별도로 알려주지 않는 경우가 많아 (예: 7일 고정) 기본값으로 설정.
     */
    fun saveTokens(accessToken: String, refreshToken: String, expiresInSeconds: Int) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        // 액세스 토큰 만료 시각 설정 (현재시간 + expiresIn초 * 1000밀리초)
        this.accessTokenExpiresAt = System.currentTimeMillis() + expiresInSeconds * 1000L

        // 리프레시 토큰 만료 시각 기본 7일 후로 설정 (서버에서 알려주면 이를 적용하는 것이 더 정확)
        this.refreshTokenExpiresAt = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
        Log.d("TokenManager", "토큰 저장됨 - accessTokenExpiresAt=${accessTokenExpiresAt}, refreshTokenExpiresAt=${refreshTokenExpiresAt}")
    }

    // 저장된 모든 토큰 정보를 삭제 (로그아웃 시 등)
    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}

