package com.example.shinhan_qna_aos.info.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
     * 학생 정보 제출 함수
     * - 현재 UI 상태에서 입력된 학생 정보를 서버에 multipart 폼으로 전송
     * - 이미지 파일은 Uri를 받아서 압축 후 전송
     * - 전송 성공 시 가입 상태 조회를 하여 사용자 상태 및 정보를 앱 내 저장소에 갱신
     * - 이에 따라 UI 상태의 navigateTo 값 변경하여 화면 전환 유도
     */
    fun submitStudentInfo(context: Context, navController: NavController) {
        viewModelScope.launch {
            val accessToken = data.accessToken ?: return@launch

            val imageUri = _uiState.value.imageUri
            val compressedFile = ImageUtils.compressImage(context, imageUri)
            if (compressedFile == null) return@launch

            val submitResult = infoRepository.submitStudentInfo(accessToken, _uiState.value, compressedFile)
            if (submitResult.isSuccess) {
                val checkResult = infoRepository.checkUserStatus(accessToken)
                if (checkResult.isSuccess) {
                    val wrapper = checkResult.getOrNull()
                    if (wrapper != null) {
                        navigateBasedOnUserStatus(wrapper)
                    }
                }
            }
        }
    }

    /**
     * 학생 인증 상태 및 경고/차단 상태 기반 화면 분기 함수
     * @param userResponseWrapper 서버에서 받은 유저 & 경고 정보 래퍼
     */
    fun navigateBasedOnUserStatus(userResponseWrapper: UserResponseWrapper) {
        val user = userResponseWrapper.user
        val warnings = userResponseWrapper.warnings.orEmpty()

        // 로컬 데이터 저장 및 갱신
        data.userStatus = user.status
        data.userName = user.name
        data.userEmail = user.email
        data.studentCertified = user.studentCertified ?: false

        // 경고/차단 여부 확인
        val hasBlock = warnings.any { it.status == "차단" }
//        val hasWarning = warnings.any { it.status == "경고" }

        // 분기 경로 결정: 차단 > 경고 > 학생인증 상태 > 가입 상태
        val destination = when {
//            hasBlock -> "block"         // 차단 화면(예시) -> 만들어야함
//            hasWarning -> "warning"     // 경고 화면 -> 일단 해놨는데 경고에 대한 분기는 없을 예정
            !data.studentCertified -> "info" // 학생 인증 안 된 경우 인증 화면
            user.status == "가입 완료" -> "main"  // 가입 완료 시 메인 화면
            user.status == "가입 대기 중" -> "wait"  // 가입 대기 중시 대기 화면
            else -> "info"              // 그 외 정보 입력 화면
        }
        _navigationRoute.value = destination
    }
    // 초기 진입시 사용자 상태 체크하고 네비게이션 경로 결정 (예: AppNavigation에서 호출)
    fun determineInitialRoute(showOnboarding: Boolean, loginResult: LoginResult) {
        viewModelScope.launch {
            if (showOnboarding) {
                _navigationRoute.value = "onboarding"
                return@launch
            }

            if (loginResult is LoginResult.Success) {
                if (data.isAdmin) {
                    _navigationRoute.value = "main"
                    return@launch
                }

                val accessToken = data.accessToken
                if (accessToken.isNullOrEmpty()) {
                    _navigationRoute.value = "login"
                    return@launch
                }

                val didSubmitInfo = data.studentCertified
                if (didSubmitInfo) {
                    val checkResult = infoRepository.checkUserStatus(accessToken)
                    if (checkResult.isSuccess) {
                        val wrapper = checkResult.getOrNull()
                        if (wrapper != null) {
                            navigateBasedOnUserStatus(wrapper)
                            return@launch
                        }
                    }
                    _navigationRoute.value = "info"
                    return@launch
                } else {
                    _navigationRoute.value = "info"
                    return@launch
                }
            }

            _navigationRoute.value = "login"
        }
    }
}
