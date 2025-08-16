package com.example.shinhan_qna_aos.login.api

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data

/**
 * AuthRepository
 * - 서버 통신(APIInterface) + 로컬 토큰 저장(LoginManager) 책임
 * - ViewModel은 Repository의 함수를 호출만 함
 */
class AuthRepository(
    private val apiInterface: APIInterface,
    private val data: Data // 토큰, 관리자 여부 저장 객체
) {

    //  관리자 로그인
    suspend fun loginAdmin(id: String, password: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.AdminLoginData(AdminRequest(id, password))
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it, isAdmin = true) // 관리자 여부 저장
                } ?: throw Exception("로그인 응답이 비어있습니다.")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    //  카카오 로그인
    suspend fun loginWithKakao(accessToken: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.KakaoAuthCode(accessToken)
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it, isAdmin = false) // 기본적으로 일반 사용자
                } ?: throw Exception("응답 데이터 없음")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    //  구글 로그인
    suspend fun loginWithGoogle(authCode: String): Result<LoginTokensResponse> {
        return runCatching {
            val response = apiInterface.GoogleAuthCode(authCode)
            if (response.isSuccessful) {
                response.body()?.also {
                    saveTokens(it, isAdmin = false)
                } ?: throw Exception("응답 데이터 없음")
            } else {
                throw Exception("로그인 실패: ${response.code()} ${response.message()}")
            }
        }
    }

    //  토큰 재발급 처리
    suspend fun refreshTokenIfNeeded(): Boolean {
        if (!data.isAccessTokenExpired()) return true // 아직 유효하면 그대로 사용

        val refreshToken = data.refreshToken ?: return false
        if (data.isRefreshTokenExpired()) return false

        val response = apiInterface.ReToken(RefreshTokenRequest(refreshToken))
        return if (response.isSuccessful) {
            response.body()?.let { saveTokens(it, isAdmin = data.isAdmin) }
            true
        } else {
            false
        }
    }

    //  토큰 및 관리자 여부 저장
    private fun saveTokens(tokens: LoginTokensResponse, isAdmin: Boolean = false) {
        data.saveTokens(tokens.accessToken, tokens.refreshToken, tokens.expiresIn)
        data.isAdmin = isAdmin
    }
}
