package com.example.shinhan_qna_aos.servepage.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.ManagerButton
import com.example.shinhan_qna_aos.TopBar
import com.jihan.lucide_icons.lucide
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TitleContentCountButton
import com.example.shinhan_qna_aos.TitleContentLikeButton
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.servepage.manager.api.DeclarationRepository
import com.example.shinhan_qna_aos.servepage.manager.api.DeclarationUIModel
import com.example.shinhan_qna_aos.servepage.manager.api.DeclarationViewModel
@Composable
fun DeclarationScreen(
    declarationRepository: DeclarationRepository,
    postRepository: PostRepository,
    data: Data,
    navController: NavController
) {
    val declarationViewModel: DeclarationViewModel = viewModel(factory = SimpleViewModelFactory {DeclarationViewModel(declarationRepository)})
    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory {PostViewModel(postRepository)})

    val declarationList = declarationViewModel.declarationList
    val postList by remember { derivedStateOf { postViewModel.postList } }

    LaunchedEffect(Unit) {
        declarationViewModel.LoadDeclaration()
        postViewModel.loadPosts()
    }

    val combinedList = remember(declarationList, postList) {
        declarationList.mapNotNull { decl ->
            val post = postList.find { it.postID == decl.postId }
            post?.let {
                DeclarationUIModel(
                    postID = it.postID,
                    title = it.title,
                    content = it.content,
                    likeCount = it.likeCount,
                    flagsCount = it.flagsCount,
                    banCount = it.banCount.toIntOrNull() ?: 0,
                    status = it.responseState
                )
            }
        }
    }

    Box {
        Column {
            TopBar("신고 검토", onNavigationClick = {navController.popBackStack()})

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 50.dp)
            ) {
                items(combinedList) { item ->
                    TitleContentCountButton(
                        title = item.title,
                        content = item.content,
                        likeCount = item.likeCount,
                        isAdmin = data.isAdmin,
                        flagsCount = item.flagsCount,
                        banCount = item.banCount,
                        onClick = { navController.navigate("declaration/${item.postID}") }
                    )
                    Divider()
                }
            }
        }
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

@Composable
fun DeclarationOpenScreen(postId: String, navController: NavController, postRepository: PostRepository,) {

    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory {PostViewModel(postRepository)})

    // 첫 진입시 상세 데이터 불러오기
    LaunchedEffect(postId) {
        postViewModel.loadPostDetail(postId)
    }

    val postDetail = postViewModel.selectedPost

    Column(modifier = Modifier.systemBarsPadding().fillMaxSize().background(Color.White)) {
        TopBar(null) { navController.navigate("declaration") { popUpTo("declaration/${postId}") { inclusive = true } } }
        LazyColumn() {
            item {
                DetailContent(
                    title = postDetail?.title.toString(),
                    content = postDetail?.content ?: ""
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ManagerButton(
                        icon = lucide.arrow_big_left_dash,
                        label = "반려",
                        background = Color(0xffFC4F4F),
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    ManagerButton(
                        icon = lucide.flag,
                        label = "경고",
                        background = Color(0xffFF9F43),
                        onClick = { }
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DeclarationPreview(){
//    DeclarationScreen()
}