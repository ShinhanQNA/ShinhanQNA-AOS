package com.example.shinhan_qna_aos.login.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ManagerLoginViewModel(
    private val repository: AuthRepository //  Repository 의존
) : ViewModel() {

    var state by mutableStateOf(ManagerLoginData())

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> get() = _loginResult

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
                    _loginResult.value =
                        LoginResult.Success(it.accessToken, it.refreshToken, it.expiresIn)
                }
                .onFailure { e ->
                    _loginResult.value = LoginResult.Failure(-1, e.localizedMessage ?: "관리자 로그인 실패")
                }
        }
    }
}
