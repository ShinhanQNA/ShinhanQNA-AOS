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

class OnboardingRepository(private val context: Context) {

    companion object {
        val ONBOARDED = booleanPreferencesKey("onboarded")
    }

    suspend fun isOnboarded(): Boolean {
        // 저장된 값이 없으면 false 반환하도록 ?: false 추가
        return context.dataStore.data.first()[ONBOARDED] ?: false
    }

    suspend fun setOnboarded(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDED] = value
        }
    }
}
