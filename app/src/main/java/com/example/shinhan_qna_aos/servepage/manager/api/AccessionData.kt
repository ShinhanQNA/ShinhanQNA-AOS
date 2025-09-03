package com.example.shinhan_qna_aos.servepage.manager.api

import com.google.gson.annotations.SerializedName

data class AccessionData(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("students") val students: String, // studentid
    @SerializedName("year") val year: String,
    @SerializedName("department") val department: String
)

data class AccessionDetailData(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("students") val students: String, // studentid
    @SerializedName("year") val year: String,
    @SerializedName("department") val department: String,
    @SerializedName("imagePath") val imagePath: String
)
