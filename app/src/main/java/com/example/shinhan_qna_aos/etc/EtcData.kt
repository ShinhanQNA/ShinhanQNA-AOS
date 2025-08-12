package com.example.shinhan_qna_aos.etc

data class Write(
   val postId: Int,
   val title: String,
   val content: String,
   val likes: Int,
   val date: String,
   val imagePath: String?,
   val category: String,
   val status: String,
   val email: String,
   val year: String
)