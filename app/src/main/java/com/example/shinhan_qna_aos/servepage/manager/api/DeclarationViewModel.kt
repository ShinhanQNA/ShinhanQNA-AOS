package com.example.shinhan_qna_aos.servepage.manager.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DeclarationViewModel(private val declarationRepository: DeclarationRepository) : ViewModel() {

    var declarationList by mutableStateOf<List<DeclarationData>>(emptyList())

    // 신고되 게시글 조회
    fun LoadDeclaration() {
        viewModelScope.launch {
            declarationRepository.loadDeclaration()
                .onSuccess { declarationList = it }
        }
    }
}