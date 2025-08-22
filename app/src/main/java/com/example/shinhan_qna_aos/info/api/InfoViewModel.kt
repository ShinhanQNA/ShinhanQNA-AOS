package com.example.shinhan_qna_aos.info.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.ImageUtils
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.login.api.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            val accessToken = data.accessToken ?: return@launch

            val imageUri = _uiState.value.imageUri
            val compressedFile = ImageUtils.compressImage(context, imageUri)
            if (compressedFile == null) return@launch

            val submitResult = infoRepository.submitStudentInfo(accessToken, _uiState.value, compressedFile)
            if (submitResult.isSuccess) {
                // 서버에 정보 제출 성공, 가입 상태 조회해서 화면 분기
                val userStatusResult = infoRepository.checkUserStatus(accessToken)
                userStatusResult.getOrNull()?.let {
                    updateLocalAndNavigate(it)
                }
            }
        }
    }

    /**
     * 서버에서 유저 정보를 받아 로컬데이터 갱신 + 네비게이션 경로 상태 업데이트
     */
    fun updateLocalAndNavigate(userResponseWrapper: UserResponseWrapper) {
        val user = userResponseWrapper.user
        val warnings = userResponseWrapper.warnings.orEmpty()

        // 로컬 데이터 업데이트
        data.userStatus = user.status
        data.userName = user.name
        data.userEmail = user.email
        data.studentCertified = user.studentCertified ?: false // null-safe

        // 경고/차단 여부 확인
        val hasBlock = warnings.any { it.status == "차단" }

        // 분기 경로 결정: 차단 > 경고 > 학생인증 상태 > 가입 상태
        val destination = when {
            hasBlock -> "block"         // 차단 화면 -> 만들어야함
//            hasWarning -> "warning"     // 경고 화면 -> 일단 해놨는데 경고에 대한 분기는 없을 예정
            !data.studentCertified -> "info" // 학생 인증 안 된 경우 인증 화면
            user.status == "가입 완료" -> "main"  // 가입 완료 시 메인 화면
            user.status == "가입 대기 중" -> "wait"  // 가입 대기 중시 대기 화면
            else -> "info"              // 그 외 정보 입력 화면
        }
        _navigationRoute.value = destination
    }

    /**
     * 앱 진입 시 상태조회 → 화면분기용 함수(예: 로그인 등)
     */
    fun checkAndNavigateUserStatus(accessToken: String) {
        viewModelScope.launch {
            val result = infoRepository.checkUserStatus(accessToken)
            result.getOrNull()?.let {
                updateLocalAndNavigate(it)
            }
        }
    }

    /**
     * 앱 진입 시 최초 라우트 결정
     */
    // (진입 시: showOnboarding/로그인 결과에 따라)
    fun decideInitialRoute(loginResult: LoginResult, data: Data) {
        viewModelScope.launch {
            if (data.onboarding) {
                _navigationRoute.value = "onboarding"
                return@launch
            }
            // 로그인 성공
            if (loginResult is LoginResult.Success) {
                if (data.isAdmin) {
                    _navigationRoute.value = "main"
                    return@launch
                }
                val accessToken = data.accessToken
                if (accessToken.isNullOrEmpty()) {
                    _navigationRoute.value = "login"
                } else {
                    // → 바로 유저 상태 체크 & 분기 (핵심!)
                    checkAndNavigateUserStatus(accessToken)
                }
                return@launch
            }
            // 로그인 실패
            _navigationRoute.value = "login"
        }
    }
}
