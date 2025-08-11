package com.example.shinhan_qna_aos.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(private val repository: OnboardingRepository) : ViewModel() {

    val pages = listOf(
        OnboardingData(R.drawable.android_mockup1),
        OnboardingData(R.drawable.android_mockup2)
    )

    private val _showOnboarding = MutableStateFlow<Boolean?>(null)
    val showOnboarding: StateFlow<Boolean?> = _showOnboarding

    init {
        viewModelScope.launch {
            val onboarded = repository.isOnboarded()
            _showOnboarding.value = !onboarded
        }
    }

    fun setOnboarded() {
        viewModelScope.launch {
            repository.setOnboarded(true)
            _showOnboarding.value = false
        }
    }
    companion object {
        fun provideFactory(
            repository: OnboardingRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OnboardingViewModel(repository) as T
            }
        }
    }
}
