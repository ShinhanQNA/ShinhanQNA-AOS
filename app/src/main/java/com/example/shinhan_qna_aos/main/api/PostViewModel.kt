package com.example.shinhan_qna_aos.main.api

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

    var selectedPost by mutableStateOf<PostDetail?>(null)
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
                .onSuccess { selectedPost = it }
                .onFailure { errorMessage = it.message }
        }
    }
}