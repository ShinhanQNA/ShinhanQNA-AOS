package com.example.shinhan_qna_aos.info

import com.example.shinhan_qna_aos.API.APIInterface
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class InfoRepository(private val apiInterface: APIInterface) {

    suspend fun checkUserStatus(accessToken: String): Result<UserCheckResponse> {
        return try {
            val response = apiInterface.UserCheck("Bearer $accessToken")
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitStudentInfo(
        accessToken: String,
        data: LoginData,
        imageFile: File
    ): Result<Unit> {
        return try {
            val studentIdPart = data.students.toString().toRequestBody("text/plain".toMediaType())
            val yearPart = data.year.toString().toRequestBody("text/plain".toMediaType())
            val namePart = data.name.toRequestBody("text/plain".toMediaType())
            val departmentPart = data.department.toRequestBody("text/plain".toMediaType())
            val rolePart = data.role.toRequestBody("text/plain".toMediaType())
            val imagePart = imageFile.let {
                val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            val response = apiInterface.InfoStudent(
                accessToken = "Bearer $accessToken",
                students = studentIdPart,
                name = namePart,
                department = departmentPart,
                year = yearPart,
                role = rolePart,
                image = imagePart
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
