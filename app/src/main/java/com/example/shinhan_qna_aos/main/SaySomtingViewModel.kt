package com.example.shinhan_qna_aos.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.API.APIRetrofit.apiService
import com.example.shinhan_qna_aos.login.TokenManager
import kotlinx.coroutines.launch


class SaySomtingViewModel(
    private val tokenManager: TokenManager
): ViewModel() {

    var postList by mutableStateOf<List<TitleContentLike>>(emptyList())
        private set

    var isAdmin by mutableStateOf(false) // 실제 로그인 정보에 따라 셋팅

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadPosts()
    }

    fun loadPosts(size: Int = 300, sort: String = "day") {
        val accessToken = tokenManager.accessToken ?: run {
            errorMessage = "로그인 토큰이 없습니다."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = apiService.getPosts(
                    code = "Bearer $accessToken",
                    size = size,
                    sort = sort
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // API Post -> UI용 TitleContentLike 변환
                        postList = body.posts.map { post ->
                            TitleContentLike(
                                postID = post.PostID,
                                title = post.Title,
                                content = post.Content,
                                likeCount = post.Likes,
                                // flagsCount, banCount 같은 경우 서버가 제공하지 않으면 0 으로 초기화
                                flagsCount = 0,
                                banCount = 0,
                                responseState = post.Status
                            )
                        }
                    } else {
                        errorMessage = "응답 데이터가 없습니다."
                    }
                } else {
                    errorMessage = "서버 오류: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "네트워크 오류: ${e.localizedMessage ?: "알 수 없음"}"
            } finally {
                isLoading = false
            }
        }
    }
}