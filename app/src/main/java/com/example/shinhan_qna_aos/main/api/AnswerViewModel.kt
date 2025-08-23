package com.example.shinhan_qna_aos.main.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnswerViewModel(private val repository: AnswerRepository) : ViewModel() {

    private val _answerList = MutableStateFlow<List<Answer>>(emptyList())
    val answerList = _answerList.asStateFlow()

    // 현재 선택된 단일 답변 상태
    private val _selectedAnswer = MutableStateFlow<Answer?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    // 전체 답변 리스트 로드
    fun loadAnswers() {
        viewModelScope.launch {
            val result = repository.getAnswers()
            if (result.isSuccess) {
                val list = result.getOrDefault(emptyList())
                _answerList.value = list
            } else {
                // 에러 처리 로직 추가 가능
            }
        }
    }

    // id 기준 단일 답변을 리스트에서 검색하여 선택 상태에 설정
    fun selectAnswerById(id: Int) {
        val answer = _answerList.value.find { it.id == id }
        _selectedAnswer.value = answer
    }
}
