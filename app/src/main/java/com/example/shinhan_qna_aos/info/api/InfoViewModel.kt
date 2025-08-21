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

    // UI 상태를 나타내는 StateFlow, 외부에는 읽기전용으로 노출
    private val _uiState = MutableStateFlow(InfoUiState())
    var uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    // 이름 변경 시 호출, infoData 내 name 값 갱신
    fun onNameChange(newName: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(name = newName))
    }

    // 학번(학생 번호) 변경 시 호출, 문자열을 정수로 변환 후 갱신 (변환 실패 시 0으로 초기화)
    fun onStudentIdChange(newId: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(students = newId.toIntOrNull() ?: 0))
    }

    // 학년 변경 시 호출, "학년" 접미사 제거 후 정수로 변환하며 기본값 0 처리
    fun onGradeChange(newGrade: String) {
        val gradeInt = newGrade.removeSuffix("학년").toIntOrNull() ?: 0
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(year = gradeInt))
    }

    // 전공(학과) 변경 시 호출, infoData 내 department 값 갱신
    fun onMajorChange(newMajor: String) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(department = newMajor))
    }

    // 이미지 변경 시 호출, 단순히 Uri 값만 infoData에 저장
    fun onImageChange(uri: Uri) {
        _uiState.value =
            _uiState.value.copy(infoData = _uiState.value.infoData.copy(imageUri = uri))
    }

    /**
     * 학생 정보 제출 함수
     * - 현재 UI 상태에서 입력된 학생 정보를 서버에 multipart 폼으로 전송
     * - 이미지 파일은 Uri를 받아서 압축 후 전송
     * - 전송 성공 시 가입 상태 조회를 하여 사용자 상태 및 정보를 앱 내 저장소에 갱신
     * - 이에 따라 UI 상태의 navigateTo 값 변경하여 화면 전환 유도
     */
     fun submitStudentInfo(context: Context) {
        viewModelScope.launch {
            val accessToken = data.accessToken ?: return@launch

            val imageUri = _uiState.value.infoData.imageUri
            val compressedFile = ImageUtils.compressImage(context, imageUri)
            if (compressedFile == null) return@launch

            val result = infoRepository.submitStudentInfo(accessToken, _uiState.value.infoData, compressedFile)

            if (result.isSuccess) {
                val infoResponse = result.getOrNull()
                Log.d("InfoViewModel", "학생 정보 제출 서버 응답: ${infoResponse?.message}")

                val checkResult = infoRepository.checkUserStatus(accessToken)
                if (checkResult.isSuccess) {
                    val userInfo = checkResult.getOrNull()
                    Log.d("InfoViewModel", "checkUserStatus - userInfo from server: $userInfo")

                    if (userInfo != null) {
                        data.userStatus = userInfo.status
                        data.userName = userInfo.name
                        data.userEmail = userInfo.email
                        data.studentCertified = userInfo.studentCertified!!

                        _uiState.value = _uiState.value.copy(
                            navigateTo = when (userInfo.status) {
                                "가입 완료" -> "main"
                                "가입 대기 중" -> "wait"
                                else -> "info"
                            }
                        )
                    }
                } else {
                    Log.d("InfoViewModel", "가입 상태 조회 실패: ${checkResult.exceptionOrNull()?.message}")
                }
            } else {
                Log.d("InfoViewModel", "학생 정보 제출 실패: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}
