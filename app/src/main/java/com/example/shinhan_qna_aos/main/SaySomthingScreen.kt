package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.login.api.LoginManager
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel

@Composable
fun SaySomthingScreen(postRepository: PostRepository, loginManager: LoginManager, navController:NavController) {
    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository,loginManager) })

    val dataList = postViewModel.postList
    val isAdmin = postViewModel.isAdmin

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
                onClick = {
                    if (isAdmin) {
                    navController.navigate("managerPostDetail/${data.postID}")
                } else {
                    navController.navigate("postDetail/${data.postID}")
                }
                }
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