package com.example.shinhan_qna_aos.login

data class LoginData(
    val name: String = "",
    val studentId: String = "",
    val grade: String? = "",
    val major: String? = ""
)

data class ManagerLoginData(
    val managerId: String = "",
    val managerPassword: String = ""
)