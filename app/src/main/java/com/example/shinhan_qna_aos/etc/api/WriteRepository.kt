package com.example.shinhan_qna_aos.etc.api

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class WriteRepository(
    private val apiInterface: APIInterface,
    private val data: Data,
) {
    suspend fun writeBoards(
        title: String,
        content: String,
        category: String = "없음",
        imageFile: File? // ← File 직접 전달받기
    ): Result<Write> {
        val accessToken = data.accessToken
            ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        return try {
            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageFile?.let { file ->
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            val response = apiInterface.writeBoards(
                accessToken = "Bearer $accessToken",
                title = titleBody,
                content = contentBody,
                category = categoryBody,
                image = imagePart
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
