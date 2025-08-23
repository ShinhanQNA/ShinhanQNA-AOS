package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel

@Composable
fun SaySomthingScreen(postRepository: PostRepository, data: Data, navController:NavController) {
    val postViewModel: PostViewModel =
        viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository) })

    val dataList = postViewModel.postList

    LaunchedEffect(Unit) {
        postViewModel.loadPosts()
    }

    LazyColumn {
        items(dataList, key = { it.postID }) { board ->
            TitleContentLikeButton(
                title = board.title,
                content = board.content,
                likeCount = board.likeCount,
                isAdmin = data.isAdmin,
                flagsCount = board.flagsCount,
                banCount = warningStatusToBanCount(board.banCount).toInt(),
                onClick = { navController.navigate("writeOpen/${board.postID}") }
            )
            Divider()
        }
    }
}

fun warningStatusToBanCount(warningStatus: String): String = when (warningStatus) {
    "없음" -> "0"
    "경고" -> "1"
    "차단" -> "2"
    else -> "0"
}

@Composable
@Preview(showBackground = true)
fun SayScreenPreview(){
//    SaySomthingScreen()
}