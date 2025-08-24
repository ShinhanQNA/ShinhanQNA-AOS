package com.example.shinhan_qna_aos.main.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TWPostViewModel(private val repository: TWPostRepository) : ViewModel() {

    // UI에서 관찰할 StateFlow (초기값 빈 리스트)
    private val _opinions = MutableStateFlow<List<GroupID>>(emptyList())
    val opinions: StateFlow<List<GroupID>> = _opinions.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadOpinions() {
        viewModelScope.launch {
            Log.d("TWPostViewModel", "의견 데이터 요청 시작")
            val result = repository.fetchThreeWeekOpinions()
            if (result.isSuccess) {
                Log.d("TWPostViewModel", "의견 데이터 요청 성공")
                _opinions.value = result.getOrNull() ?: emptyList()
            } else {
                val error = result.exceptionOrNull()?.localizedMessage ?: "알 수 없는 오류"
                Log.e("TWPostViewModel", "의견 데이터 요청 실패: $error")
            }
        }
    }
}
