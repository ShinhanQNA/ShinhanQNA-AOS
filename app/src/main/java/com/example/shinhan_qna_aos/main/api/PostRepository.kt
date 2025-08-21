package com.example.shinhan_qna_aos.main.api

import androidx.compose.runtime.State
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data

class PostRepository(
    private val apiInterface: APIInterface,
    private val data: Data
) {

    /**
     * 게시글 목록 조회
     * @param size 가져올 개수
     * @param sort 정렬 방식 (예: "day")
     */
    suspend fun getPosts(): Result<List<TitleContentLike>> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val response = apiInterface.getPosts("Bearer $accessToken")
            if (response.isSuccessful) {
                val body = response.body()?.map {
                    val banCountInt = when (it.warningStatus) {
                        "없음" -> "0"
                        "경고" -> "1"
                        "차단" -> "2"
                        else -> "0" // 기본값
                    }
                    TitleContentLike(
                        postID = it.postID,
                        title = it.title,
                        content = it.content,
                        likeCount = it.likes,
                        flagsCount = it.reportCount,
                        banCount = banCountInt,
                        responseState = it.status
                    )
                } ?: emptyList()
                Result.success(body)
            } else {
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 상세 조회
     */
    suspend fun getPostDetail(postId: String): Result<PostDetail> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val response = apiInterface.getPostsDetail("Bearer $accessToken", postId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("상세 데이터 없음"))
            } else {
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 좋아요 취소
     */
    suspend fun PostUnlike(postId: Int): Result<PostLike> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val response = apiInterface.PostUnlike("Bearer $accessToken", postId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("좋아요 취소 실패"))
            } else {
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 좋아요
     */
    suspend fun PostLike(postId: Int): Result<PostLike> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val response = apiInterface.PostLike("Bearer $accessToken", postId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception(""))
            } else {
                // 400 에러 + "이미 공감한 게시글입니다." 메시지면 공감취소 API 시도
                val errorBody = response.errorBody()?.string() ?: ""
                if (response.code() == 400 && errorBody.contains("이미 공감한 게시글")) {
                    // 좋아요 취소 API도 결과 반환
                    PostUnlike(postId)
                } else {
                    Result.failure(Exception("서버 오류: ${response.code()} ${response.message()} $errorBody"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 신고
     */
    suspend fun Postflag(postId: Int, reportReason: String?): Result<PostFlag> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다"))

        return try {
            val body = ReportReasonBody(reportReason)  // Body에 신고사유 세팅
            val response = apiInterface.PostFlag(
                accessToken = "Bearer $accessToken",
                postId = postId,
                reportReasonBody = body
            )
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("신고 실패"))
            } else {
                val errorBody = response.errorBody()?.string() ?: ""
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()} $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * 게시글 취소
     */
    suspend fun PostDelete(postId: Int): Result<Unit> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다"))

        return try {
            val response = apiInterface.PostDelete("Bearer $accessToken", postId)
            if (response.isSuccessful) {
                Result.success(Unit) // response.body() 체크 없이 성공 처리
            } else {
                val errorBody = response.errorBody()?.string() ?: ""
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()} $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
