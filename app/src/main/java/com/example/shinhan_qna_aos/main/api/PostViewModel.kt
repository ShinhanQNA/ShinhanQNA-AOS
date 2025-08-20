package com.example.shinhan_qna_aos.main.api

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.Data
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,   // API 호출 담당
) : ViewModel() {

    // 게시글 리스트
    var postList by mutableStateOf<List<TitleContentLike>>(emptyList())

    var selectedPost by mutableStateOf<Post?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 좋아요 상태 (해당 게시글에 사용자가 좋아요 눌렀는지)
    var hasLiked by mutableStateOf(false)
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
            postRepository.getPosts(300, "day") // 나중에 관리자인 경우 sort 선택 가능(day,year,like) 유저는 day
                .onSuccess {
                    postList = it
                }
                .onFailure { errorMessage = it.message }
        }
    }

    /**
     * 게시글 상세 로드
     */
    fun loadPostDetail(postId: String) {
        viewModelScope.launch {
            postRepository.getPostDetail(postId)
                .onSuccess {
                    selectedPost = it
                    Log.d("PostViewModel", "loadPostDetail onSuccess, imagePath: ${it.imagePath}")
                }
                .onFailure {
                    errorMessage = it.message
                    Log.e("PostViewModel", "loadPostDetail error: ${it.message}")
                }
        }
    }

    // 좋아요 토글 함수: 좋아요 <-> 좋아요 취소
    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            try {
                val result = if (!hasLiked) {
                    postRepository.PostLike(postId)
                } else {
                    postRepository.PostUnlike(postId)
                }
                if (result.isSuccess) {
                    val postLike = result.getOrNull()
                    hasLiked = !hasLiked
                    loadPostDetail(postId.toString())
                } else {
                    errorMessage = result.exceptionOrNull()?.message
                    Log.e("PostViewModel", "toggleLike 실패: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "toggleLike 예외: ${e.message}")
            }
        }
    }
}