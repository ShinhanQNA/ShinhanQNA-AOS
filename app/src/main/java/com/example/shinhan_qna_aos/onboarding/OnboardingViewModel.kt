package com.example.shinhan_qna_aos.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: OnboardingRepository
) : ViewModel() {
    // 온보딩 화면 표시 여부 상태 저장용 플로우
    private val _showOnboarding = MutableStateFlow<Boolean?>(null)
    val showOnboarding: StateFlow<Boolean?> = _showOnboarding

    init {
        // 뷰모델 초기화 시 저장소에서 온보딩 완료 여부 확인 후 상태값 설정
        viewModelScope.launch {
            _showOnboarding.value = !repository.isOnboarded()
        }
    }
    // 온보딩 완료 상태 저장 후 화면 숨김 처리 함수
    fun setOnboarded() {
        viewModelScope.launch {
            repository.setOnboarded(true)
            _showOnboarding.value = false // 화면 숨김 표시
        }
    }
}
