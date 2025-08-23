package com.example.shinhan_qna_aos.main.api

import android.util.Log
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data
import retrofit2.Response

class TWPostRepository (
    private val apiInterface: APIInterface,
    private val data: Data
){
    // 그룹ID, 정렬조건을 넘겨 3주 의견 데이터 호출 suspend 함수
    suspend fun fetchThreeWeekOpinions(groupId: Int, sort: String): Result<TWPostData> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))
        return try {
            val response = apiInterface.ThreeWeekPost("Bearer $accessToken", groupId, sort)
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("TWPostRepository", "API 호출 성공 - 데이터 수: ${it.opinions.size}")
                    Result.success(it)
                } ?: run {
                    Log.e("TWPostRepository", "API 호출 성공했으나 body가 null")
                    Result.failure(Exception("응답 데이터가 없습니다."))
                }
            } else {
                Log.e("TWPostRepository", "API 호출 실패 - 코드: ${response.code()}, 메시지: ${response.message()}")
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("TWPostRepository", "API 호출 중 예외 발생", e)
            Result.failure(e)
        }
    }
}