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

    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    var state by mutableStateOf(
        LoginData()
    )

    var compressedImageFile: File? by mutableStateOf(null)

    var apiResponseMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    companion object {
        private const val TAG = "InfoViewModel"
    }

    fun onNameChange(newName: String) {
        state = state.copy(name = newName)
    }

    fun onStudentIdChange(newId: String) {
        val idInt = newId.toIntOrNull() ?: 0
        state = state.copy(studentId = idInt)
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
                var quality = 100
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
                    quality -= 5
                } while (fileSizeKB > maxFileSizeMB * 1024 && quality > 5)
                Log.d(TAG, "compressImage finished: file=${compressedFile.absolutePath}, size=${fileSizeKB}KB")
                compressedFile
            } catch (e: Exception) {
                Log.e(TAG, "compressImage: Exception during compression", e)
                null
            }
        }
    }

    fun submitStudentInfo(api: APIInterface, accessToken: String, context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            apiResponseMessage = null

            Log.d(TAG, "submitStudentInfo started with state: $state")

            if (state.imageUri == Uri.EMPTY || compressedImageFile == null) {
                errorMessage = "사진 첨부는 필수입니다."
                Log.e(TAG, errorMessage!!)
                isLoading = false
                return@launch
            }

            try {
                Log.d(TAG, "Preparing multipart request body")
                val studentIdPart = state.studentId.toString().toRequestBody("text/plain".toMediaType())
                val namePart = state.name.toRequestBody("text/plain".toMediaType())
                val departmentPart = state.department.toRequestBody("text/plain".toMediaType())
                val yearPart = state.year.toString().toRequestBody("text/plain".toMediaType())
                val rolePart = state.role.toRequestBody("text/plain".toMediaType())

                val imagePart = compressedImageFile?.let { file ->
                    Log.d(TAG, "Image file for upload: ${file.absolutePath}, size=${file.length() / 1024}KB")
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                if (imagePart == null) {
                    errorMessage = "사진이 존재하지 않습니다."
                    Log.e(TAG, errorMessage!!)
                    isLoading = false
                    return@launch
                }

                val response = api.InfoStudent(
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
                    Log.d(TAG, "API call successful: $apiResponseMessage")
                } else {
                    val errBody = response.errorBody()?.string()
                    when (response.code()) {
                        401 -> Log.e(TAG, "Unauthorized (401): $errBody")
                        500 -> Log.e(TAG, "Server error (500): $errBody")
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
}

