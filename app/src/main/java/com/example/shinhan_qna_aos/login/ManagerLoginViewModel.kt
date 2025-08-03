package com.example.shinhan_qna_aos.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ManagerLoginViewModel : ViewModel() {
    var state by mutableStateOf(ManagerLoginData())

    fun onAdminIdChange(newId: String) {
        state = state.copy(managerId = newId)
    }

    fun onAdminPasswordChange(newPw: String) {
        state = state.copy(managerPassword = newPw)
    }
}