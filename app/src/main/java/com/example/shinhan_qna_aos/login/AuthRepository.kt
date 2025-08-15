package com.example.shinhan_qna_aos.login

import com.example.shinhan_qna_aos.API.APIInterface

/**
 * AuthRepository
 * - 서버 통신(APIInterface) + 로컬 토큰 저장(LoginManager) 책임
 * - ViewModel은 Repository의 함수를 호출만 함
 */
class AuthRepository(
    private val apiInterface: APIInterface,
    private val loginManager: LoginManager
) {
    // 관리자 로그인
    suspend fun loginAdmin(id: String, password: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.AdminLoginData(AdminRequest(id, password))
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it, isAdmin = true)
                } ?: throw Exception("로그인 응답이 비어있습니다.")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    // 카카오 로그인
    suspend fun loginWithKakao(accessToken: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.KakaoAuthCode(accessToken)
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it)
                } ?: throw Exception("응답 데이터 없음")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    // 구글 로그인
    suspend fun loginWithGoogle(authCode: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.GoogleAuthCode(authCode)
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it)
                } ?: throw Exception("응답 데이터 없음")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    // 토큰 재발급 처리
    suspend fun refreshTokenIfNeeded(): Boolean {
        if (!loginManager.isAccessTokenExpired()) return true

        val refreshToken = loginManager.refreshToken ?: return false
        if (loginManager.isRefreshTokenExpired()) return false

        val response = apiInterface.ReToken(RefreshTokenRequest(refreshToken))
        if (response.isSuccessful) {
            response.body()?.let {
                saveTokens(it)
            }
            return true
        }
        return false
    }

    // 토큰 저장 메서드 (LoginTokensResponse 기반)
    private fun saveTokens(tokens: LoginTokensResponse, isAdmin: Boolean = false) {
        loginManager.saveTokens(tokens.accessToken, tokens.refreshToken, tokens.expiresIn)
        if (isAdmin) loginManager.isAdmin = true
    }
}
