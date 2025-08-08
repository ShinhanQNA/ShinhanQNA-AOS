package com.example.shinhan_qna_aos.info

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.login.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class InfoViewModel(
    private val api: APIInterface,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val TAG = "InfoViewModel"

    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    var state by mutableStateOf(LoginData())
    var compressedImageFile: File? by mutableStateOf(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // 상태 체크 후 경로 이동용
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> get() = _navigateTo

    fun onNameChange(newName: String) {
        state = state.copy(name = newName)
    }

    fun onStudentIdChange(newId: String) {
        val idInt = newId.toIntOrNull() ?: 0
        state = state.copy(students = idInt)
    }

    fun onGradeChange(newGrade: String) {
        val yearInt = newGrade.replace("학년", "").toIntOrNull() ?: 0
        state = state.copy(year = yearInt)
    }

    fun onMajorChange(newMajor: String) {
        state = state.copy(department = newMajor)
    }

    fun onImageChange(context: Context, uri: Uri) {
        Log.d(TAG, "onImageChange called with uri=$uri")
        state = state.copy(imageUri = uri)
        viewModelScope.launch {
            compressedImageFile = compressImage(context, uri, 10)
            Log.d(TAG, "onImageChange: compressedImageFile=${compressedImageFile?.absolutePath}")
        }
    }

    private suspend fun compressImage(context: Context, imageUri: Uri, maxFileSizeMB: Int): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                if (inputStream == null) return@withContext null
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                if (originalBitmap == null) return@withContext null
                val compressedFile = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
                var quality = 100
                var fileSizeKB: Long

                do {
                    if (compressedFile.exists()) compressedFile.delete()
                    FileOutputStream(compressedFile).use { outputStream ->
                        val compressed = originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                        if (!compressed) return@withContext null
                        outputStream.flush()
                    }
                    fileSizeKB = compressedFile.length() / 1024
                    quality -= 5
                } while (fileSizeKB > maxFileSizeMB * 1024 && quality > 5)

                compressedFile
            } catch (e: Exception) {
                null
            }
        }
    }

    fun submitStudentInfo() {
        val accessToken = tokenManager.accessToken
        if (accessToken.isNullOrEmpty()) {
            errorMessage = "로그인 정보가 없습니다. 다시 로그인 해주세요."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            if (state.imageUri == Uri.EMPTY || compressedImageFile == null) {
                errorMessage = "사진 첨부는 필수입니다."
                isLoading = false
                return@launch
            }

            try {
                val studentIdPart = state.students.toString().toRequestBody("text/plain".toMediaType())
                val yearPart = state.year.toString().toRequestBody("text/plain".toMediaType())
                val namePart = state.name.toRequestBody("text/plain".toMediaType())
                val departmentPart = state.department.toRequestBody("text/plain".toMediaType())
                val rolePart = state.role.toRequestBody("text/plain".toMediaType())
                val imagePart = compressedImageFile?.let { file ->
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                if (imagePart == null) {
                    errorMessage = "사진이 존재하지 않습니다."
                    isLoading = false
                    return@launch
                }

                val response = api.InfoStudent(
                    accessToken = "Bearer $accessToken",
                    students = studentIdPart,
                    name = namePart,
                    department = departmentPart,
                    year = yearPart,
                    role = rolePart,
                    image = imagePart
                )

                if (response.isSuccessful) {
                    Log.d(TAG, "InfoStudent 성공! 상태 확인 로직 진입");
                    checkUserStatusAndNavigate()
                } else {
                    val errBody = response.errorBody()?.string()
                    errorMessage = errBody ?: "알 수 없는 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "통신 오류가 발생했습니다: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // UserCheck API를 호출해서 상태확인 및 네비게이션 route 결정
    fun checkUserStatusAndNavigate() {
        val accessToken = tokenManager.accessToken
        if (accessToken.isNullOrEmpty()) {
            Log.d(TAG, "토큰 없음. 로그인 필요.")  // 추가 로그
            errorMessage = "로그인 정보가 없습니다. 다시 로그인해 주세요."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                Log.d(TAG, "UserCheck API 호출 전")  // 추가 로그
                val response = api.UserCheck(accessToken = "Bearer $accessToken")
                Log.d(TAG, "UserCheck API 호출 완료. response.isSuccessful=${response.isSuccessful}")
                if (response.isSuccessful) {
                    val userCheck = response.body()
                    Log.d(TAG, "응답 userCheck: $userCheck")
                    Log.d(TAG, "userCheck.status: [${userCheck?.status}]")
                    Log.d(TAG, "userCheck.name: [${userCheck?.name}]")
                    if (userCheck != null) {
                        when (userCheck.status) {
                            "가입 대기 중" -> {
                                Log.d(TAG, "상태가 '가입 대기 중'이라서 대기화면으로 이동합니다.")
                                _navigateTo.value = "wait/${userCheck.name}"
                            }
                            "가입 완료" -> {
                                Log.d(TAG, "상태가 '가입 완료'라서 메인화면으로 이동합니다.")
                                _navigateTo.value = "main"
                            }
                            else -> {
                                Log.d(TAG, "알 수 없는 사용자 상태: ${userCheck.status}")
                                errorMessage = "알 수 없는 사용자 상태입니다."
                            }
                        }
                    } else {
                        Log.d(TAG, "userCheck가 null임.")
                        errorMessage = "사용자 정보 조회 실패"
                    }
                } else {
                    Log.d(TAG, "UserCheck 응답 실패: ${response.errorBody()?.string()}")
                    errorMessage = response.errorBody()?.string() ?: "상태 조회 실패"
                }
            } catch (e: Exception) {
                Log.d(TAG, "통신 에러: ${e.localizedMessage}")
                errorMessage = "통신 오류 발생: ${e.localizedMessage}"
            } finally {
                isLoading = false
                Log.d(TAG, "checkUserStatusAndNavigate 완료 (isLoading=false)")
            }
        }
    }


    fun resetNavigateTo() {
        _navigateTo.value = null
    }
}
