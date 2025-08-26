package com.example.shinhan_qna_aos.servepage.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    private val _noticesList = MutableStateFlow<List<Notices>>(emptyList())
    val noticesList = _noticesList.asStateFlow()

    // 현재 선택된 단일 공지 상태
    private val _selectedNotices = MutableStateFlow<Notices?>(null)
    val selectedNotices = _selectedNotices.asStateFlow()

    // 전체 공지 리스트 로드
    fun loadNotification(id: Int? = null) {
        viewModelScope.launch {
            val result = repository.getNotification()
            if (result.isSuccess) {
                val list = result.getOrDefault(emptyList())
                _noticesList.value = list

                id?.let {
                    val answer = list.find { it.id == id }
                    _selectedNotices.value = answer
                }
            }
        }
    }

    //class NotificationWritingViewModel:ViewModel() {
//    var state by mutableStateOf(
//        NotificationWriteData(
//            title = null,
//            content = null
//        )
//    )
//
//    fun onTitleChange(newTitle: String) {
//        state = state.copy(title = newTitle)
//    }
//
//    fun onContentChange(newContent: String) {
//        state = state.copy(content = newContent)
//    }
//}
}
