package com.example.shinhan_qna_aos.etc

import android.net.Uri

data class WriteData (
    val title: String? ="",
    val content: String? ="",
    val imageUri : Uri?
)