package com.example.shinhan_qna_aos.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.example.shinhan_qna_aos.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }
}