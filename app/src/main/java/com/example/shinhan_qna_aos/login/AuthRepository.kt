package com.example.shinhan_qna_aos.login

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.google.gson.Gson

class AuthRepository(
    private val apiService: APIInterface = APIRetrofit.apiService
) {

    // 카카오 로그인 서버 호출
    suspend fun loginWithKakao(token: String): Result<LoginBackendResponse> {
        return try {
            val response = apiService.kakaoAccessToken("kakaotoken $token")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 없습니다."))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResp = parseErrorBody(errorBody)
                Result.failure(Exception(errorResp?.message ?: "카카오 로그인 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 구글 로그인 서버 호출
    suspend fun loginWithGoogle(idToken: String): Result<LoginBackendResponse> {
        return try {
            val response = apiService.googleLogin("googletoken $idToken")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("서버 응답이 없습니다."))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResp = parseErrorBody(errorBody)
                Result.failure(Exception(errorResp?.message ?: "구글 로그인 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 에러 바디를 ApiErrorResponse 객체로 파싱
    private fun parseErrorBody(errorBody: String?): ApiErrorResponse? {
        return try {
            errorBody?.let { Gson().fromJson(it, ApiErrorResponse::class.java) }
        } catch (e: Exception) {
            null
        }
    }
}