package com.example.shinhan_qna_aos.info.api

import android.net.Uri
import com.google.gson.annotations.SerializedName

//API 학생 정보 받음
data class InfoResponse(
    val message:String,
)

//API 학생 정보를 multipart 폼으로 제출
data class InfoData(
    val students: Int = 0,        // 학번
    val name: String = "",         // 이름
    val department: String = "",   // 학과
    val year: Int = 0,             // 학년
    val role: String = "학생",     // 역할
    val studentCertified: Boolean = true,
    val imageUri: Uri = Uri.EMPTY  // 이미지 Uri
)

//API 유저 조회로 받음
data class UserCheckResponse(
    val email : String?="",
    val name : String,
    val token : String?="",
    val role : String,
    val year : Int,
    val department : String,
    val studentCardImagePath : String?="",
    val students : Int,
    val status : String,
    @SerializedName("studentCertified") // Gson 사용 시
    val studentCertified: Boolean? = null
)

// ui
data class InfoUiState(
    val infoData: InfoData = InfoData(),
    val navigateTo: String? = null
)