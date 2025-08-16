//package com.example.shinhan_qna_aos.info
//
//import android.content.Context
//import android.net.Uri
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.shinhan_qna_aos.ImageUtils
//import com.example.shinhan_qna_aos.Data
//import com.jihan.lucide_icons.lucide.user
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.io.File
//
//class InfoViewModel(
//    private val infoRepository: InfoRepository,
//    private val data: Data
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(InfoUiState())
//    var uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()
//
//    val user = _uiState.value.data
//    private val TAG = "InfoViewModel"
//
//    // 학생 정보 상태 갱신 함수들
//    fun onNameChange(newName: String) {
//        _uiState.value = _uiState.value.copy(data = _uiState.value.data.copy(name = newName))
//    }
//    fun onStudentIdChange(newId: String) {
//        _uiState.value = _uiState.value.copy(data = _uiState.value.data.copy(students = newId.toIntOrNull() ?: 0))
//    }
//    fun onGradeChange(newGrade: String) {
//        val gradeInt = newGrade.removeSuffix("학년").toIntOrNull() ?: 0
//        _uiState.value = _uiState.value.copy(data = _uiState.value.data.copy(year = gradeInt))
//    }
//    fun onMajorChange(newMajor: String) {
//        _uiState.value = _uiState.value.copy(data = _uiState.value.data.copy(department = newMajor))
//    }
//    fun onImageChange(uri: Uri) {
//        // 이미지 Uri를 파일 경로로 변환 후 UI 상태에 반영 (간단히 Uri 저장)
//        _uiState.value = _uiState.value.copy(data = _uiState.value.data.copy(imageUri = uri))
//    }
//
//    // 가입 요청 제출 함수
//    fun submitStudentInfo(context: Context){
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
//            val accessToken = data.accessToken ?: return@launch
//
//            // 이미지 Uri를 압축 후 File로 변환
//            val imageUri = _uiState.value.data.imageUri
//            val compressedFile = ImageUtils.compressImage(context, imageUri, 10)
//            if (compressedFile == null) {
//                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "이미지 압축 실패")
//                return@launch
//            }
//
//            // 서버에 학생 정보(압축이미지 포함) 제출
//            val result = infoRepository.submitStudentInfo(accessToken, _uiState.value.data, compressedFile)
//            if (result.isSuccess) {
//                // 가입 요청 성공 시 가입 상태 재조회
//                val checkResult = infoRepository.checkUserStatus(accessToken)
//                if (checkResult.isSuccess) {
//                    val status = checkResult.getOrNull()?.status ?: ""
//                    data.userName = user.name
//                    data.userInfoSubmitted = true   // 정보 제출 기록!
//                    _uiState.value = _uiState.value.copy(
//                        isLoading = false,
//                        navigateTo = when (status) {
//                            "가입 완료" -> "main"
//                            "가입 대기 중" -> "wait/${data.userName}"
//                            else -> "info"
//                        }
//                    )
//                } else {
//                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "가입 상태 조회 실패")
//                }
//            } else {
//                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.localizedMessage ?: "가입 요청 실패")
//            }
//        }
//    }
//}
