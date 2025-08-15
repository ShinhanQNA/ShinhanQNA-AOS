package com.example.shinhan_qna_aos.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// Context 확장 프로퍼티로 DataStore 선언 (반드시 클래스 바깥에 선언)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")
// 온보딩 관련 저장소 클래스
class OnboardingRepository(private val context: Context) {
    // 온보딩 완료 여부를 저장하는 Boolean 키 정의
    companion object {
        val ONBOARDED = booleanPreferencesKey("onboarded")
    }
    // 저장된 온보딩 완료 여부 읽기 (값 없으면 false 반환)
    suspend fun isOnboarded(): Boolean {
        return context.dataStore.data.first()[ONBOARDED] ?: false
    }
    // 온보딩 완료 여부 저장
    suspend fun setOnboarded(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDED] = value
        }
    }
}
