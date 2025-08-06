package com.example.shinhan_qna_aos.login.loginserver

import android.util.Log
import com.example.shinhan_qna_aos.API.APIRetrofit
import com.example.shinhan_qna_aos.login.ApiErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun sendKakaoOpenIdTokenToServer(idToken: String, TAG: String) {
    val idTokenHeader = "kakaoopenid $idToken"
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = APIRetrofit.apiService.kakaoOpenIdToken(idTokenHeader)
            when (response.code()) {
                200 -> {
                    val loginResponse = response.body()
                    Log.d(TAG, "서버 인증 성공: $loginResponse")
                    // TODO: 토큰 정보를 저장하고 메인 화면으로 이동
                }
                401 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorResponse = Gson().fromJson(errorBodyString, ApiErrorResponse::class.java)
                        Log.d(TAG, "서버 인증 실패 (401): ${errorResponse.message}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 인증 실패 (401), JSON 파싱 오류: $errorBodyString")
                    }
                }
                500 -> {
                    val errorBodyString = response.errorBody()?.string()
                    try {
                        val errorJson = Gson().fromJson(errorBodyString, Map::class.java)
                        Log.d(TAG, "서버 오류 (500): ${errorJson["error"]}")
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "서버 오류 (500), JSON 파싱 오류: $errorBodyString")
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.d(TAG, "알 수 없는 응답 코드: ${response.code()}, 오류: $errorBody")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "네트워크 오류: ${e.message}")
        }
    }
}