package com.example.shinhan_qna_aos.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIRetrofit.apiService
import com.example.shinhan_qna_aos.login.LoginManager
import kotlinx.coroutines.launch


class SaySomtingViewModel(
    private val loginmanager: LoginManager
): ViewModel() {

    var postList by mutableStateOf<List<TitleContentLike>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isAdmin by mutableStateOf(false) // 관리자 설정

    var selectedPost by mutableStateOf<PostDetail?>(null) // 상세 선택
        private set

    init {
        isAdmin = loginmanager.isAdmin()
        loadPosts()
    }

    fun loadPosts() {
        val accessToken = loginmanager.accessToken ?: run {
            errorMessage = "로그인 토큰이 없습니다."
            return
        }

        viewModelScope.launch {
            try {
                val response = apiService.getPosts("Bearer $accessToken", size=300, sort="day") // size 관련 수정
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        postList = body.map { post ->
                            TitleContentLike(
                                postID = post.postID,
                                title = post.title,
                                content = post.content,
                                likeCount = post.likes,
                                flagsCount = 0,
                                banCount = 0,
                                responseState = post.status
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
            }
        }
    }
    // 상세조회
    fun loadPostDetail(postId: Int, onComplete: (() -> Unit)? = null) {
        val accessToken = loginmanager.accessToken ?: return
        isLoading = true
        viewModelScope.launch {
            try {
                val response = apiService.getPostsDetail("Bearer $accessToken", postId)
                if (response.isSuccessful) {
                    selectedPost = response.body()
                } else {
                    errorMessage = "상세 조회 실패"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
                onComplete?.invoke()
            }
        }
    }
}