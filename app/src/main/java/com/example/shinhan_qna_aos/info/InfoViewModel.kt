package com.example.shinhan_qna_aos.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shinhan_qna_aos.login.LoginData

class InfoViewModel : ViewModel() {
    // 드롭다운 선택지
    val gradeOptions = listOf("1학년", "2학년", "3학년", "4학년")
    val majorOptions = listOf("소프트웨어융합학과")

    // 상태 관리
    var state by mutableStateOf(
        LoginData(
            grade = null,
            major = null
        )
    )

    fun onNameChange(newName: String) {
        state = state.copy(name = newName)
    }

    fun onStudentIdChange(newId: String) {
        state = state.copy(studentId = newId)
    }

    fun onGradeChange(newGrade: String) {
        state = state.copy(grade = newGrade)
    }

    fun onMajorChange(newMajor: String) {
        state = state.copy(major = newMajor)
    }
}
