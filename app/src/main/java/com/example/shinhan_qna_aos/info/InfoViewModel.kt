package com.example.shinhan_qna_aos.info

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinhan_qna_aos.API.APIInterface
import kotlinx.coroutines.Dispatchers
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

class InfoViewModel : ViewModel() {

    // 드롭다운 옵션: 학년, 전공
    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    // 전체 상태 관리 (Compose가 관찰 가능하도록 선언)
    var state by mutableStateOf(
        LoginData(
            studentId = "",
            name = "",
            grade = null,
            major = null,
            role = "사용자",
            imageUri = null
        )
    )

    // 압축된 이미지 파일 저장소
    var compressedImageFile: File? by mutableStateOf(null)

    // API 호출 및 로딩 상태, 메시지
    var apiResponseMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // 상태 변경 함수
    fun onNameChange(newName: String) { state = state.copy(name = newName) }
    fun onStudentIdChange(newId: String) { state = state.copy(studentId = newId) }
    fun onGradeChange(newGrade: String) { state = state.copy(grade = newGrade) }
    fun onMajorChange(newMajor: String) { state = state.copy(major = newMajor) }

    // 이미지 Uri 변화 시 호출: 이미지 압축 수행
    fun onImageChange(context: Context, uri: Uri?) {
        state = state.copy(imageUri = uri)
        if (uri == null) {
            compressedImageFile = null
            return
        }

        viewModelScope.launch {
            compressedImageFile = compressImage(context, uri, 10)
        }
    }

    // 이미지 압축 함수 (10MB 이하로 반복 압축)
    private suspend fun compressImage(context: Context, imageUri: Uri, maxFileSizeMB: Int): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri) ?: return@withContext null
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                val compressedFile = File(context.cacheDir, "compressed_image.jpg")

                var quality = 100
                var fileSizeKB: Int

                do {
                    val outputStream = FileOutputStream(compressedFile)
                    originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    fileSizeKB = (compressedFile.length() / 1024).toInt()
                    quality -= 5
                } while (fileSizeKB > maxFileSizeMB * 1024 && quality > 5)

                compressedFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // 학생 정보 인증 API 호출
    fun submitStudentInfo(api: APIInterface, accessToken: String, context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            apiResponseMessage = null

            try {
                // 각 필드를 RequestBody로 변환
                val studentIdPart = state.studentId.toRequestBody("text/plain".toMediaType())
                val namePart = state.name.toRequestBody("text/plain".toMediaType())
                val departmentPart = (state.major ?: "").toRequestBody("text/plain".toMediaType())
                val yearPart = (state.grade ?: "").toRequestBody("text/plain".toMediaType())
                val rolePart = (state.role).toRequestBody("text/plain".toMediaType())

                // 압축된 이미지가 없으면 오류 처리
                val imagePart = compressedImageFile?.let { file ->
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                if (imagePart == null) {
                    isLoading = false
                    errorMessage = "사진이 존재하지 않습니다."
                    return@launch
                }

                // API 호출
                val response: Response<InfoData> = api.InfoStudent(
                    accessToken = "Bearer $accessToken",
                    studentId = studentIdPart,
                    name = namePart,
                    department = departmentPart,
                    year = yearPart,
                    role = rolePart,
                    image = imagePart
                )

                if (response.isSuccessful) {
                    apiResponseMessage = response.body()?.message ?: "성공"
                } else {
                    when (response.code()) {
                        401 -> {
                            val errorBody = response.errorBody()?.string()
                            errorMessage = errorBody ?: "사진이 존재하지 않습니다."
                        }
                        500 -> {
                            val errorBody = response.errorBody()?.string()
                            errorMessage = errorBody ?: "서버 에러가 발생했습니다."
                        }
                        else -> {
                            errorMessage = "알 수 없는 오류: ${response.code()}"
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "통신 오류가 발생했습니다: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}

object FileUtil {
    // Uri를 임시 파일로 변환하는 함수
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Uri를 열 수 없습니다.")
        val file = File(context.cacheDir, "temp_image")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        outputStream.close()
        inputStream.close()
        return file
    }
}