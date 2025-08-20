package com.example.shinhan_qna_aos.etc.api

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.ImageUtils
import kotlinx.coroutines.launch
import java.io.File

class WritingViewModel(
    private val writeRepository: WriteRepository
) : ViewModel() {

    // 파일 압축 결과 저장 (이미지 파일)
    var compressedImageFile: File? by mutableStateOf(null)
        private set

    // Compose에서 상태 관찰 (제목, 내용, 카테고리, 이미지 URI)
    var state by mutableStateOf(WriteData(title = "", content = "", category = null, imageUri = null))
        private set

    // 제목 입력 변경 시 호출
    fun onTitleChange(newTitle: String) {
        state = state.copy(title = newTitle)
    }

    // 내용 입력 변경 시 호출
    fun onContentChange(newContent: String) {
        state = state.copy(content = newContent)
    }

    // 이미지 선택 시 호출: URI저장 + 비동기로 압축 파일 저장
    fun onImageChange(context: Context, uri: Uri) {
        state = state.copy(imageUri = uri)
        viewModelScope.launch {
            // 이미지 압축 유틸 호출(압축 결과를 File로 저장)
            compressedImageFile = ImageUtils.compressImage(context, uri)
        }
    }

    // 글쓰기 화면 진입 시 기존 데이터 세팅(수정모드)
    fun setEditMode(
        title: String,
        content: String,
        category: String? = null,
        imageUri: Uri? = null,
        context: Context
    ) {
        state = state.copy(
            title = title,
            content = content,
            category = category,
            imageUri = imageUri
        )
        if (imageUri != null) {
            // 바로 이미지 파일 압축 준비
            viewModelScope.launch {
                compressedImageFile = ImageUtils.compressImage(context, imageUri)
            }
        }
    }

    // 게시글 작성
    fun uploadPost(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = writeRepository.writeBoards(
                title = state.title,
                content = state.content,
                imageFile = compressedImageFile // ← 여기서 이미지 압축 전달
            )
            result
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "알 수 없는 오류 발생") }
        }
    }
    // 게시글 수정
    fun updatePost(
        postId: String,
        title: String,
        content: String,
        category: String = "없음",
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val result = writeRepository.updatePost(
                postId = postId,
                title = title,
                content = content,
                category = category,
                imageFile = compressedImageFile // 압축 이미지 파일(없으면 기존 이미지 그대로)
            )
            result
                .onSuccess { onSuccess() }
                .onFailure { onError() }
        }
    }
}
