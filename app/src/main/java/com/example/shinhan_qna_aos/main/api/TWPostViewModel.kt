package com.example.shinhan_qna_aos.main.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TWPostViewModel (private val repository: TWPostRepository) : ViewModel(){

    private val _opinionsData = MutableStateFlow<TWPostData?>(null)
    val opinionsData: StateFlow<TWPostData?> = _opinionsData.asStateFlow()


    // 현재 선택된 groupId, sort 보관
    private var currentGroupId: Int? = null
    private var currentSort: String = "date"

    /**
     * groupId, sort 변경 시 호출해서 API 재호출 처리
     */
    fun loadOpinions(accessToken: String?, groupId: Int, sort: String) {
        // 중복 호출 방지
        if (groupId == currentGroupId && sort == currentSort && opinionsData.value != null) {
            Log.d("TWPostViewModel", "동일 groupId 및 sort로 중복 API 호출 방지")
            return
        }
        currentGroupId = groupId
        currentSort = sort

        viewModelScope.launch {
            Log.d("TWPostViewModel", "의견 데이터 요청 시작 - groupId: $groupId, sort: $sort")
            val result = repository.fetchThreeWeekOpinions(groupId, sort)
            if (result.isSuccess) {
                _opinionsData.value = result.getOrNull()
                Log.d("TWPostViewModel", "의견 데이터 요청 성공")
            } else {
                result.exceptionOrNull()?.localizedMessage ?: "알 수 없는 오류"
                Log.e("TWPostViewModel", "의견 데이터 요청 실패: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}