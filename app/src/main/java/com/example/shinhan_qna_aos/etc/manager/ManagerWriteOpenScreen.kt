package com.example.shinhan_qna_aos.etc.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.LikeFlagBan
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.login.LoginManager
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun ManagerWriteOpenScreen(
    postId: Int,
    postRepository:PostRepository,
    loginManager: LoginManager,
    navController: NavController,
    isNotice: Boolean = false // true면 공지사항, false면 일반글
) {
    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository,loginManager) })
    val postDetail = postViewModel.selectedPost

    // 처음 진입 시 API 호출
    LaunchedEffect(postId) {
        postViewModel.loadPostDetail(postId)
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

            // 공지사항이 아닌 경우 좋아요/신고 영역 표시
            if (!isNotice) {
                Spacer(modifier = Modifier.height(16.dp))
                if (postDetail != null) {
                    LikeFlagBan(
                        likeCount = postDetail.likes,
                        flagsCount = 0, // 여긴 아직 값이 안 옴
                        banCount = 34 // 여긴 아직 값이 안 옴
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            ManagerFunctionButton(isNotice)

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

@Composable
fun ManagerFunctionButton(isNotice: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ManagerButton(
            icon = if (isNotice) lucide.trash else lucide.list,
            label = if (isNotice) "삭제" else "검토",
            background = Color(0xffFC4F4F)
        )

        Spacer(modifier = Modifier.width(16.dp))

        ManagerButton(
            icon = if (isNotice) R.drawable.square_pen else lucide.flag,
            label = if (isNotice) "수정" else "경고",
            background = if (isNotice) Color.Black else Color(0xffFF9F43)
        )
    }
}

@Composable
fun ManagerButton(icon: Int, label: String, background: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
        Text(
            text = label,
            color = Color.White,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WriteOpenScreenPreview(){
//    ManagerWriteOpenScreen()
//    FunctionButton()
}