package com.example.shinhan_qna_aos.onboarding

import androidx.lifecycle.ViewModel
import com.example.shinhan_qna_aos.R

class OnboardingViewModel : ViewModel() {
    val pages = listOf(
        OnboardingData(R.drawable.onboarding1, "타이틀1", "설명1"),
        OnboardingData(R.drawable.onboarding2, "타이틀2", "설명2")
    )
}