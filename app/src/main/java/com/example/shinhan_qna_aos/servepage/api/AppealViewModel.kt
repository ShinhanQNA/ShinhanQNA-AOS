package com.example.shinhan_qna_aos.servepage.api

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AppealViewModel(
    private val appealRepository: AppealRepository
) : ViewModel() {

    // 이의 신청시 응답으로 오는 게시글 관련리스트
    var appealList by mutableStateOf<List<AppealData>>(emptyList())
        private set

    // 여러 개의 BlockReasonData 객체 리스트로 상태 저장
    var blockReasonDataList by mutableStateOf<List<BlockReasonData>>(emptyList())

    fun loadAppeals() {
        viewModelScope.launch {
            appealRepository.appeal()
                .onSuccess { list ->
                    appealList = list
                }
                .onFailure { error ->
                    error.message
                }
        }
    }

//    fun loadBlockReason(email: String) {
//        viewModelScope.launch {
//            appealRepository.blockReason(email)
//                .onSuccess { data ->
//                    blockReasonDataList = data
//                    Log.d("blockReasonDataList", blockReasonDataList.toString())
//                }
//                .onFailure { error ->
//                    error.message
//                }
//        }
//    }
}
