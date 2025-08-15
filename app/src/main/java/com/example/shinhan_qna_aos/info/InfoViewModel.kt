package com.example.shinhan_qna_aos.info

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.ImageUtils
import com.example.shinhan_qna_aos.login.api.LoginManager
import kotlinx.coroutines.launch
import java.io.File

class InfoViewModel(
    private val repository: InfoRepository,
    private val loginManager: LoginManager
) : ViewModel() {

    var uiState by mutableStateOf(InfoUiState())

    private val TAG = "InfoViewModel"

    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    var compressedImageFile: File? by mutableStateOf(null)

    fun onNameChange(newName: String) {
        uiState = uiState.copy(data = uiState.data.copy(name = newName))
    }

    fun onStudentIdChange(newId: String) {
        val idInt = newId.toIntOrNull() ?: 0
        uiState = uiState.copy(data = uiState.data.copy(students = idInt))
    }

    fun onGradeChange(newGrade: String) {
        val yearInt = newGrade.replace("학년", "").toIntOrNull() ?: 0
        uiState = uiState.copy(data = uiState.data.copy(year = yearInt))
    }

    fun onMajorChange(newMajor: String) {
        uiState = uiState.copy(data = uiState.data.copy(department = newMajor))
    }

    fun onImageChange(context: Context, uri: Uri) {
        uiState = uiState.copy(data = uiState.data.copy(imageUri = uri))
        viewModelScope.launch {
            compressedImageFile = ImageUtils.compressImage(context, uri, 10)
        }
    }

    // 사용자 정보/상태 조회 후 목적 화면으로 이동
    fun checkUserStatusAndNavigate() {
        val token = loginManager.accessToken
        if (token.isNullOrEmpty()) {
            uiState = uiState.copy(errorMessage = "로그인 정보가 없습니다. 다시 로그인해주세요.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = repository.checkUserStatus(token)
            uiState = uiState.copy(isLoading = false)

            result.onSuccess { userCheck ->
                when (userCheck.status) {
                    "가입 대기 중" -> uiState = uiState.copy(navigateTo = "wait/${userCheck.name}")
                    "가입 완료" -> uiState = uiState.copy(navigateTo = "main")
                }
            }.onFailure {
                uiState = uiState.copy(errorMessage = it.message)
            }
        }
    }

    fun submitStudentInfo() {
        val token = loginManager.accessToken
        if (token.isNullOrEmpty()) {
            uiState = uiState.copy(errorMessage = "로그인 정보가 없습니다.")
            return
        }

        if (uiState.data.imageUri == Uri.EMPTY || compressedImageFile == null) {
            uiState = uiState.copy(errorMessage = "사진 첨부는 필수입니다.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = repository.submitStudentInfo(token, uiState.data, compressedImageFile!!)
            uiState = uiState.copy(isLoading = false)

            result.onSuccess {
                checkUserStatusAndNavigate()
            }.onFailure {
                uiState = uiState.copy(errorMessage = it.message)
            }
        }
    }
}
