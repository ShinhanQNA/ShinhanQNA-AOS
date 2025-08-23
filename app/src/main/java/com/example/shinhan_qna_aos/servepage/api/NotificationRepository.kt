package com.example.shinhan_qna_aos.servepage.api

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data

//class NotificationRepository(
//    private val apiInterface: APIInterface,
//    private val data: Data
//) {
//    // 답변 리스트 받아오기 API 호출
//    suspend fun getAnswers(): Result<List<Answer>> {
//        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))
//        return try {
//            val response = apiInterface.AnswerPost("Bearer $accessToken")
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
//            } else {
//                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}
