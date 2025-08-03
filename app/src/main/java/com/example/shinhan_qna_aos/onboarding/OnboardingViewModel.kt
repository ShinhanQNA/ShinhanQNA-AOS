package com.example.shinhan_qna_aos.onboarding

import androidx.lifecycle.ViewModel
import com.example.shinhan_qna_aos.R

class OnboardingViewModel : ViewModel() {
    val pages = listOf(
        OnboardingData(R.drawable.android_mockup1),
        OnboardingData(R.drawable.android_mockup2)
    )
}