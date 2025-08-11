package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.login.LoginManager

@Composable
fun SaySomthingScreen(saySomtingViewModel: SaySomtingViewModel,navController:NavController) {
    val context = LocalContext.current

    val dataList = saySomtingViewModel.postList
    val isAdmin = saySomtingViewModel.isAdmin

    LazyColumn {
        items(dataList, key = { it.postID }) { data ->
            var responseState by remember { mutableStateOf(data.responseState) }
            TitleContentLikeButton(
                title = data.title,
                content = data.content,
                likeCount = data.likeCount,
                isAdmin = isAdmin,
                flagsCount = data.flagsCount,
                banCount = data.banCount,
                responseState = responseState,
                onResponseStateChange = { newState ->
                    responseState = newState
                },
                onClick = { navController.navigate("postDetail/${data.postID}") }
            )
            Divider()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SayScreenPreview(){
//    SaySomthingScreen()
}