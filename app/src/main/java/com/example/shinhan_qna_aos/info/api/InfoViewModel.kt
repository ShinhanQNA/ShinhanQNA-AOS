package com.example.shinhan_qna_aos.info.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.ImageUtils
import com.example.shinhan_qna_aos.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(
    private val infoRepository: InfoRepository,
    private val data: Data
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    var uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    // 학생 정보 상태 갱신 함수들
    fun onNameChange(newName: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(name = newName))
    }

    fun onStudentIdChange(newId: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(students = newId.toIntOrNull() ?: 0))
    }

    fun onGradeChange(newGrade: String) {
        val gradeInt = newGrade.removeSuffix("학년").toIntOrNull() ?: 0
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(year = gradeInt))
    }

    fun onMajorChange(newMajor: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(department = newMajor))
    }

    fun onImageChange(uri: Uri) {
        // 이미지 Uri를 파일 경로로 변환 후 UI 상태에 반영 (간단히 Uri 저장)
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(imageUri = uri))
    }

    // 가입 요청 제출 및 유저 상태 조회 반영 함수
    fun submitStudentInfo(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy()
            val accessToken = data.accessToken ?: return@launch

            val imageUri = _uiState.value.infoData.imageUri
            val compressedFile = ImageUtils.compressImage(context, imageUri)
            if (compressedFile == null) { _uiState.value = _uiState.value.copy()
                return@launch
            }

            val result = infoRepository.submitStudentInfo(accessToken, _uiState.value.infoData, compressedFile)

            if (result.isSuccess) {
                val serverMsg = result.getOrNull() ?: ""
                Log.d("InfoViewModel", "학생 정보 제출 서버 응답: $serverMsg")
                // 가입 요청 성공 시 가입 상태 조회
                val checkResult = infoRepository.checkUserStatus(accessToken)
                if (checkResult.isSuccess) {
                    val userInfo = checkResult.getOrNull()

                    // status는 API 명세에 따라 널 가능성 있으므로 안전하게 null 체크 후 기본값 처리
                    val status = userInfo?.status ?: "가입 대기 중"
                    val name = userInfo?.name ?: ""
                    val email = userInfo?.email ?: ""

                    // SharedPreferences 저장
                    data.userStatus = status
                    data.userName = name
                    data.userEmail = email
                    data.userInfoSubmitted = true

                    // UI 상태 업데이트 (화면 전환용)
                    _uiState.value = _uiState.value.copy(
                        navigateTo = when (status) {
                            "가입 완료" -> "main"
                            "가입 대기 중" -> "wait"
                            else -> "info"
                        }
                    )
                } else {
                    // 실패 메시지 로그 또는 UI 알림 추가 가능
                    Log.d("InfoViewModel", "가입 상태 조회 실패: ${checkResult.exceptionOrNull()?.message}")
                    // 필요하면 UI에 에러 상태 반영 가능
                }
            } else {
                Log.d("InfoViewModel", "학생 정보 제출 실패: ${result.exceptionOrNull()?.message}")
                // 필요하면 UI에 에러 상태 반영 가능
            }
        }
    }
}
