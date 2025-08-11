package com.example.shinhan_qna_aos.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIRetrofit.apiService
import com.example.shinhan_qna_aos.login.LoginManager
import kotlinx.coroutines.launch


class PostViewModel(
    private val repository: PostRepository,   // API 호출 담당
    loginManager: LoginManager                 // 관리자 여부 체크용
) : ViewModel() {

    var postList by mutableStateOf<List<TitleContentLike>>(emptyList())
        private set

    var selectedPost by mutableStateOf<PostDetail?>(null)
        private set

    var isAdmin by mutableStateOf(loginManager.isAdmin()) // 관리자 여부

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        // ViewModel 초기화 시 목록 불러오기
        loadPosts()
    }

    /**
     * 게시글 목록 로드
     */
    fun loadPosts() {
        viewModelScope.launch {
            isLoading = true
            repository.getPosts(300, "day")
                .onSuccess { postList = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    /**
     * 게시글 상세 로드
     */
    fun loadPostDetail(postId: Int, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            repository.getPostDetail(postId)
                .onSuccess { selectedPost = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
            onComplete?.invoke()
        }
    }
}