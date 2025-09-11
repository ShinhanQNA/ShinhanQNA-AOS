package com.example.shinhan_qna_aos.servepage.manager.api

import com.google.gson.annotations.SerializedName

// 이의 제기 신청자 조회
data class BanClearData (
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("year") val year: String,
    @SerializedName("name") val name: String,
    @SerializedName("students") val students: String,
    @SerializedName("id") val id: Int,
    @SerializedName("department") val department: String
)

// 이의 제기 신청자 상세조회
data class BanClearUser(
    @SerializedName("id") val id: String,
    @SerializedName("boards") val boards: List<Board>,
    @SerializedName("name") val name: String,
    @SerializedName("department") val department: String,
    @SerializedName("year") val year: String,
    @SerializedName("students") val students: String
)

// 이의 제기 신청자 개별 게시글
data class Board(
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String,
    @SerializedName("reportCount") val reportCount: Int,
    @SerializedName("warningStatus") val warningStatus: String,
    @SerializedName("writerEmail") val writerEmail: String,
    @SerializedName("imagePath") val imagePath: String?
)
