package com.example.shinhan_qna_aos.info.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.ImageUtils
import com.example.shinhan_qna_aos.Data
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class InfoViewModel(
private val infoRepository: InfoRepository,
private val data: Data
) : ViewModel() {

    // UI 상태를 나타내는 StateFlow, 외부에는 읽기전용으로 노출
    private val _uiState = MutableStateFlow(InfoData())
    var uiState: StateFlow<InfoData> = _uiState.asStateFlow()

    // 네비게이션 경로 상태
    private val _navigationRoute = MutableStateFlow<String?>(null)
    val navigationRoute: StateFlow<String?> = _navigationRoute.asStateFlow()

    // 이름 변경 시 호출, infoData 내 name 값 갱신
    fun onNameChange(newName: String) {
        _uiState.value =
            _uiState.value.copy(name = newName)
    }

    // 학번(학생 번호) 변경 시 호출, 문자열을 정수로 변환 후 갱신 (변환 실패 시 0으로 초기화)
    fun onStudentIdChange(newId: String) {
        _uiState.value =
            _uiState.value.copy(students = newId.toIntOrNull() ?: 0)
    }

    // 학년 변경 시 호출, "학년" 접미사 제거 후 정수로 변환하며 기본값 0 처리
    fun onGradeChange(newGrade: String) {
        val gradeInt = newGrade.removeSuffix("학년").toIntOrNull() ?: 0
        _uiState.value =
            _uiState.value.copy(year = gradeInt)
    }

    // 전공(학과) 변경 시 호출, infoData 내 department 값 갱신
    fun onMajorChange(newMajor: String) {
        _uiState.value =
            _uiState.value.copy(department = newMajor)
    }

    // 이미지 변경 시 호출, 단순히 Uri 값만 infoData에 저장
    fun onImageChange(uri: Uri) {
        _uiState.value =
            _uiState.value.copy(imageUri = uri)
    }

    /**
     * 학생 정보 제출 (서버 업로드 후 상태 체크 및 분기)
     */
    fun submitStudentInfo(context: Context) {
        viewModelScope.launch {
            val imageUri = _uiState.value.imageUri
            val compressedFile = ImageUtils.compressImage(context, imageUri)
            if (compressedFile == null) return@launch

            val submitResult = infoRepository.submitStudentInfo(_uiState.value, compressedFile)
            if (submitResult.isSuccess) {
                checkAndNavigateUserStatus()
            }
        }
    }

    /**
     * 서버에서 유저 정보를 받아 로컬데이터 갱신 + 네비게이션 경로 상태 업데이트
     */
    fun updateLocalAndNavigate(userResponseWrapper: UserResponseWrapper) {
        val user = userResponseWrapper.user

        // 로컬 데이터 업데이트
        data.userStatus = user.status
        data.userName = user.name
        data.userEmail = user.email
        data.studentCertified = user.studentCertified ?: false

        // 화면 분기 결정
        val destination = when {
            user.status=="차단" && !data.isAppealCompleted -> "appeal1"   // 처음 차단, 이의 접수 전
            user.status=="차단" && data.isAppealCompleted -> "appeal3"    // 이미 이의 접수 완료된 경우
            user.status=="경고" -> "main" // 경고인 경우는 그냥 메인으로
            !data.studentCertified -> "info"
            user.status == "가입 완료" -> "main"
            user.status == "가입 대기 중" -> "wait"
            else -> "info"
        }

        // 무한 호출 방지: 이전 상태와 다를 때만 변경
        if (_navigationRoute.value != destination) {
            _navigationRoute.value = destination
        }
    }

    // 유저 정보 서버 조회 후 상태 갱신 및 네비게이션 분기 함수
    fun checkAndNavigateUserStatus() {
        viewModelScope.launch {
            Log.d("InfoViewModel", "checkAndNavigateUserStatus 호출됨")
            val result = infoRepository.checkUserStatus()
            result.getOrNull()?.let {
                Log.d("InfoViewModel", "checkAndNavigateUserStatus 성공 결과 반영")
                updateLocalAndNavigate(it)
            } ?: run {
                Log.e("InfoViewModel", "Failed to get user status")
            }
        }
    }
}
