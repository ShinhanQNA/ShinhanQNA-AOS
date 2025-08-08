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
    private val api: APIInterface,          // Retrofit API 인터페이스 주입
    private val tokenManager: TokenManager  // 토큰 관리 클래스 주입
) : ViewModel() {

    private val TAG = "InfoViewModel"

    // 학년, 학과 선택지(고정 리스트)
    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    // 사용자 입력 상태를 저장하는 데이터 클래스, Compose 상태로 관리
    var state by mutableStateOf(LoginData())

    // 이미지 압축 후 임시 저장 파일 (null 가능)
    var compressedImageFile: File? by mutableStateOf(null)

    // API 호출 로딩 상태
    var isLoading by mutableStateOf(false)

    // API 호출 또는 처리 중 발생한 에러 메시지
    var errorMessage by mutableStateOf<String?>(null)

    // 네비게이션 동작을 위한 상태 플로우 (UI에서 구독용)
    private val _navigateNext = MutableStateFlow(false)
    val navigateNext: StateFlow<Boolean> get() = _navigateNext

    //이름 입력값 변경 처리 함수
    fun onNameChange(newName: String) {
        state = state.copy(name = newName)
    }

    // 학번 입력값 변경 처리 함수
    fun onStudentIdChange(newId: String) {
        val idInt = newId.toIntOrNull() ?: 0
        state = state.copy(students = idInt)
    }

    // 학년 선택값 변경 처리 함수
    fun onGradeChange(newGrade: String) {
        val yearInt = newGrade.replace("학년", "").toIntOrNull() ?: 0
        state = state.copy(year = yearInt)
    }

    // 학과 선택값 변경 처리 함수
    fun onMajorChange(newMajor: String) {
        state = state.copy(department = newMajor)
    }

    /**
     * 이미지가 변경된 경우 호출
     * 1) 상태에 이미지 Uri 저장
     * 2) 코루틴에서 이미지 파일 압축 작업 수행 후 결과 저장
     */
    fun onImageChange(context: Context, uri: Uri) {
        Log.d(TAG, "onImageChange called with uri=$uri")
        state = state.copy(imageUri = uri)
        viewModelScope.launch {
            compressedImageFile = compressImage(context, uri, 10)  // 최대 10MB 크기로 압축
            Log.d(TAG, "onImageChange: compressedImageFile=${compressedImageFile?.absolutePath}")
        }
    }

    /**
     * 이미지 압축 비동기 함수
     * @param context Context - 이미지 리소스 접근용
     * @param imageUri Uri - 원본 이미지 Uri
     * @param maxFileSizeMB Int - 최대 허용 파일 크기(MB)
     * @return 파일 압축 완료된 File 또는 null (실패 시)
     *
     * Bitmap 압축을 품질(quality) 단계별로 감소시키면서 지정 크기 이하가 될 때까지 반복함.
     */
    private suspend fun compressImage(context: Context, imageUri: Uri, maxFileSizeMB: Int): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                if (inputStream == null) {
                    Log.e(TAG, "compressImage: InputStream is null")
                    return@withContext null
                }
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                if (originalBitmap == null) {
                    Log.e(TAG, "compressImage: Failed to decode bitmap from Uri")
                    return@withContext null
                }
                val compressedFile = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
                var quality = 100  // 압축 품질 100부터 시작
                var fileSizeKB: Long

                do {
                    if (compressedFile.exists()) compressedFile.delete()
                    FileOutputStream(compressedFile).use { outputStream ->
                        val compressed = originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                        if (!compressed) {
                            Log.e(TAG, "compressImage: Bitmap compression failed at quality=$quality")
                            return@withContext null
                        }
                        outputStream.flush()
                    }
                    fileSizeKB = compressedFile.length() / 1024
                    Log.d(TAG, "compressImage loop: quality=$quality, size=${fileSizeKB}KB")
                    quality -= 5  // 5씩 감소시키면서 압축 반복
                } while (fileSizeKB > maxFileSizeMB * 1024 && quality > 5)

                Log.d(TAG, "compressImage finished: file=${compressedFile.absolutePath}, size=${fileSizeKB}KB")
                compressedFile
            } catch (e: Exception) {
                Log.e(TAG, "compressImage: Exception during compression", e)
                null
            }
        }
    }

    /**
     * 학생 정보 서버 제출 함수
     *  - 토큰이 없으면 오류 처리
     *  - 이미지 유효성 검사
     *  - MultipartFormData 구성 후 API 호출
     *  - 성공 시 네비게이션 상태값 활성화
     *  - 에러 시 에러 메시지 갱신
     */
    fun submitStudentInfo() {
        val accessToken = tokenManager.accessToken
        if (accessToken.isNullOrEmpty()) {
            errorMessage = "로그인 정보가 없습니다. 다시 로그인 해주세요."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            Log.d(TAG, "submitStudentInfo started with state: $state")

            // 이미지 Uri 체크 및 압축 이미지 유효성 검사
            if (state.imageUri == Uri.EMPTY || compressedImageFile == null) {
                errorMessage = "사진 첨부는 필수입니다."
                Log.e(TAG, errorMessage!!)
                isLoading = false
                return@launch
            }

            try {
                Log.d(TAG, "Preparing multipart request body")

                // 각 필드를 text/plain RequestBody로 변환
                val studentIdPart = state.students.toString().toRequestBody("text/plain".toMediaType())
                val yearPart = state.year.toString().toRequestBody("text/plain".toMediaType())
                val namePart = state.name.toRequestBody("text/plain".toMediaType())
                val departmentPart = state.department.toRequestBody("text/plain".toMediaType())
                val rolePart = state.role.toRequestBody("text/plain".toMediaType())

                // 압축된 이미지 파일을 MultipartBody.Part로 포장
                val imagePart = compressedImageFile?.let { file ->
                    Log.d(TAG, "Image file for upload: ${file.absolutePath}, size=${file.length() / 1024}KB")
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                // 이미지 Multipart가 없으면 에러 처리 후 종료
                if (imagePart == null) {
                    errorMessage = "사진이 존재하지 않습니다."
                    Log.e(TAG, errorMessage!!)
                    isLoading = false
                    return@launch
                }

                // API 호출 - AccessToken은 Bearer 토큰 형태로 포함
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
                    Log.d(TAG, "API call successful: ${response.code()}")
                    _navigateNext.value = true  // 네비게이션 진행 신호 방출
                } else {
                    // 에러 바디를 문자열로 읽어서 로그 및 상태 업데이트
                    val errBody = response.errorBody()?.string()
                    when (response.code()) {
                        401 -> Log.e(TAG, "사진 없음 (401): $errBody")
                        500 -> Log.e(TAG, "서버 에러 (500): $errBody")
                        else -> Log.e(TAG, "Unknown error ${response.code()}: $errBody")
                    }
                    errorMessage = errBody ?: "알 수 없는 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "통신 오류가 발생했습니다: ${e.localizedMessage}"
                Log.e(TAG, "Exception while calling API", e)
            } finally {
                isLoading = false
            }
        }
    }

    /** 네비게이션 상태 초기화 함수 (네비게이션 후 UI에서 호출) */
    fun resetNavigateNext() {
        _navigateNext.value = false
    }
}
