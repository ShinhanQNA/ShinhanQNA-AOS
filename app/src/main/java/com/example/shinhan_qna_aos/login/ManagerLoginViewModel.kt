package com.example.shinhan_qna_aos.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import kotlinx.coroutines.launch

class ManagerLoginViewModel(
    private val apiInterface: APIInterface,
    private val tokenManager: TokenManager
) : ViewModel() {

    var state by mutableStateOf(ManagerLoginData())
    var loginResult by mutableStateOf<String?>(null) // 성공/실패 메시지
    var isLoginSuccess by mutableStateOf(false)
    var admin by mutableStateOf(false)
    fun onAdminIdChange(newId: String) {
        state = state.copy(managerId = newId)
    }

    fun onAdminPasswordChange(newPw: String) {
        state = state.copy(managerPassword = newPw)
    }

    fun login() {
        viewModelScope.launch {
            try {
                val request = AdminRequest(
                    id = state.managerId,
                    password = state.managerPassword
                )

                val response = apiInterface.AdminLoginData(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // 토큰 저장 (액세스+리프레시)
                        tokenManager.saveTokens(
                            body.accessToken,
                            body.refreshToken,
                            body.expiresIn
                        )
                        loginResult = "관리자 로그인 성공"
                        admin = true // 학생과 관리자 구별 유아이 작동을 위한 변수
                        isLoginSuccess = true //  성공 상태 true
                    } else {
                        loginResult = "로그인 응답이 비어있습니다."
                    }
                } else {
                    loginResult = "로그인 실패: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                loginResult = "서버 통신 실패: ${e.localizedMessage}"
            }
        }
    }
}
