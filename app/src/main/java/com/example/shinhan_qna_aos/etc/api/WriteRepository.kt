package com.example.shinhan_qna_aos.etc.api

import com.example.shinhan_qna_aos.API.APIInterface
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.main.api.PostDetail
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
    ): Result<PostDetail> {
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

            val response = apiInterface.uploadPost(
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

    suspend fun updatePost(
        postId: String,
        title: String,
        content: String,
        category: String,
        imageFile: File? = null  // 이미지 파일 optional 파라미터 추가
    ): Result<Unit> {
        val accessToken = data.accessToken ?: return Result.failure(Exception("로그인 토큰이 없습니다."))

        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())

        // 이미지가 있을 경우 MultipartBody.Part 생성
        val imagePart = if (imageFile != null) {
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        } else {
            null
        }

        return try {
            val response = apiInterface.updatePost(
                accessToken = "Bearer $accessToken",
                postsid = postId.toInt(),
                title = titleBody,
                content = contentBody,
                category = categoryBody,
                image = imagePart  // 이미지 Multipart 전송 반영
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("서버 오류: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}