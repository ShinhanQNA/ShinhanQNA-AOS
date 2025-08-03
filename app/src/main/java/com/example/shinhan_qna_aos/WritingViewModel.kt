package com.example.shinhan_qna_aos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WritingViewModel:ViewModel() {
    var state by mutableStateOf(
        WriteData(
            title = null,
            content = null
        )
    )

    fun onTitleChange(newTitle: String) {
        state = state.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        state = state.copy(content = newContent)
    }
}