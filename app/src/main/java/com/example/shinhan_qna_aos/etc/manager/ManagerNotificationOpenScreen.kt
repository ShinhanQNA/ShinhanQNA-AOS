package com.example.shinhan_qna_aos.etc.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.ManagerFunctionButton
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel

@Composable
fun ManagerNotificationOpenScreen(
    postRepository:PostRepository,
    data: Data,
    navController: NavController,
) {
    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository, data) })
    val postDetail = postViewModel.selectedPost

    // 처음 진입 시 API 호출
    LaunchedEffect(Unit) {
        postViewModel.loadPostDetail()
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            TopBar(null,  { navController.popBackStack() })

            // API에서 가져온 데이터로 표시
            postDetail?.let { DetailContent(title = it.title, content = postDetail.content) }

            Spacer(modifier = Modifier.height(32.dp))
            ManagerFunctionButton(data.isNotice)

        }
        // 배너광고는 항상 하단 고정
        Text(
            "배너광고",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Red)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WriteOpenScreenPreview(){
//    ManagerWriteOpenScreen()
//    FunctionButton()
}