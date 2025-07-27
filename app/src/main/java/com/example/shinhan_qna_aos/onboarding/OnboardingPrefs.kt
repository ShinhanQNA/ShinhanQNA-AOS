package com.example.shinhan_qna_aos.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object OnboardingPrefs {
    private val Context.dataStore by preferencesDataStore("onboarding")
    private val ONBOARDED = booleanPreferencesKey("onboarded")
    //온보딩 완료 여부를 저장하는 함수
    suspend fun setOnboarded(context: Context, value: Boolean) {
        context.dataStore.edit { it[ONBOARDED] = value }
    }
    //저장된 온보딩 완료 여부
    suspend fun isOnboarded(context: Context): Boolean {
        return context.dataStore.data.first()[ONBOARDED] == true
    }
}