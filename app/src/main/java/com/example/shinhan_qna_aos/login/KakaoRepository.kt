package com.example.shinhan_qna_aos.login

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class KakaoRepository(
    private val apiService: APIInterface = APIRetrofit.apiService
) {
    suspend fun accessToken(token: String): Result<LoginBackendResponse> =
        suspendCancellableCoroutine { continuation ->
            // Authorization 헤더에 "kakaotoken {token}" 형태로 전달
            val call = apiService.kakaoAccessToken("kakaotoken $token")

            // 비동기 호출 시작
            call.enqueue(object : Callback<LoginBackendResponse> {
                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            // HTTP 200: 성공
                            response.body()?.let {
                                continuation.resume(Result.success(it))
                            } ?: continuation.resume(Result.failure(Exception("서버 응답이 비어 있습니다.")))
                        }
                        401, 500 -> {
                            // HTTP 401 또는 500: 실패
                            val errorJson = response.errorBody()?.string()
                            val errorResp = parseErrorBody(errorJson)
                            continuation.resume(Result.failure(Exception(
                                errorResp?.message ?: "로그인 실패"
                            )))
                        }
                        else -> {
                            // 기타 예외 상태 코드
                            continuation.resume(Result.failure(Exception("알 수 없는 오류: ${response.code()}")))
                        }
                    }
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    // 네트워크 또는 호출 오류
                    continuation.resume(Result.failure(t))
                }
            })

            // Coroutine이 취소될 경우 Retrofit Call도 취소
            continuation.invokeOnCancellation { call.cancel() }
        }

    /**
     * 서버 에러 응답(JSON)을 ApiErrorResponse 객체로 파싱
     *
     * @param errorBody 실패 시 서버가 반환한 JSON 문자열
     * @return ApiErrorResponse 또는 null
     */
    private fun parseErrorBody(errorBody: String?): ApiErrorResponse? {
        return try {
            errorBody?.let { Gson().fromJson(it, ApiErrorResponse::class.java) }
        } catch (e: Exception) {
            null
        }
    }
}