package com.example.shinhan_qna_aos.etc

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

    var compressedImageFile: File? by mutableStateOf(null)
        private set

    var state by mutableStateOf(
        WriteData(title = null, content = null, imageUri = null)
    )
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onTitleChange(newTitle: String) {
        state = state.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        state = state.copy(content = newContent)
    }

    fun onImageChange(context: Context, uri: Uri) {
        state = state.copy(imageUri = uri)
        viewModelScope.launch {
            compressedImageFile = ImageUtils.compressImage(context, uri, maxFileSizeMB = 10)
        }
    }

    fun uploadPost(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (state.title.isNullOrBlank() || state.content.isNullOrBlank()) {
            onError("제목과 내용을 입력해주세요.")
            return
        }

        viewModelScope.launch {
            isLoading = true
            val result = writeRepository.writeBoards(
                title = state.title!!,
                content = state.content!!,
                imageFile = compressedImageFile // ← 여기서 Repository로 전달
            )
            isLoading = false

            result.onSuccess {
                onSuccess()
            }.onFailure {
                onError(it.message ?: "알 수 없는 오류 발생")
            }
        }
    }
}
