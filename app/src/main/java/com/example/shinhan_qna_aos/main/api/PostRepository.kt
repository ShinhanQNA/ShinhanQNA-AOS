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
    suspend fun getPosts(size: Int, sort: String): Result<List<TitleContentLike>> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val response = apiInterface.getPosts("Bearer $accessToken", size, sort)
            if (response.isSuccessful) {
                val body = response.body()?.map {
                    TitleContentLike(
                        postID = it.postID,
                        title = it.title,
                        content = it.content,
                        likeCount = it.likes,
                        flagsCount = 0, // 나중에 API
                        banCount = 0, // 나중에 API
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
}
