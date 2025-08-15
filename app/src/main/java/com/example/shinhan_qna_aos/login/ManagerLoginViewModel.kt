package com.example.shinhan_qna_aos.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import kotlinx.coroutines.launch

class ManagerLoginViewModel(
    private val repository: AuthRepository //  Repository 의존
) : ViewModel() {

    var state by mutableStateOf(ManagerLoginData())
    // 로그인 성공 여부
    var loginResult by mutableStateOf<String?>(null)
    // 로그인 성공으로 후 처리
    var isLoginSuccess by mutableStateOf(false)

    fun onAdminIdChange(newId: String) {
        state = state.copy(managerId = newId)
    }

    fun onAdminPasswordChange(newPw: String) {
        state = state.copy(managerPassword = newPw)
    }

    // ✅ 관리자 로그인
    fun login() {
        viewModelScope.launch {
            repository.loginAdmin(state.managerId, state.managerPassword)
                .onSuccess {
                    loginResult = "관리자 로그인 성공"
                    isLoginSuccess = true
                }
                .onFailure { e ->
                    loginResult = "로그인 실패: ${e.message}"
                }
        }
    }
}
