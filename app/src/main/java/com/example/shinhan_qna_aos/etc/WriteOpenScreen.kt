package com.example.shinhan_qna_aos.etc

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
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.SimpleViewModelFactory
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.Data
import com.example.shinhan_qna_aos.LikeFlagBan
import com.example.shinhan_qna_aos.ManagerFunctionButton
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun WriteOpenScreen (
    navController: NavController,
    postRepository: PostRepository,
    postId: Int,
    data: Data
) {
    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository,data) })

    val postDetail = postViewModel.selectedPost

    // 처음 진입 시 데이터 로드
    LaunchedEffect(postId) {
        postViewModel.loadPostDetail(postId)
    }

    postDetail?.let { post_detail ->
        val isOwner = data.userEmail == post_detail.email
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 50.dp)
            ) {
                TopBar(null) { navController.popBackStack() }
                DetailContent(post_detail.title, post_detail.content)
                Spacer(modifier = Modifier.height(16.dp))
                LikeFlagBan(
                    post_detail.likes,
                     0, // 나중에 API
                     34, // 나중에 API
                     data
                )
                Spacer(modifier = Modifier.height(36.dp))
                if(!data.isAdmin){ FunctionButton(isOwner) }
                ManagerFunctionButton(data.isNotice)
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
}

@Composable
fun FunctionButton(
    isOwner: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        // 첫 번째 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(Color(0xffFF9F43), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                painter = painterResource(
                    if (isOwner) R.drawable.square_pen else R.drawable.flag
                ),
                contentDescription = if (isOwner) "수정" else "신고",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text = if (isOwner) "수정" else "신고",
                color = Color.White,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 두 번째 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(
                    if (isOwner) Color(0xffFC4F4F) else Color.Black,
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                painter = painterResource(
                    if (isOwner) lucide.trash else lucide.thumbs
                ),
                contentDescription = if (isOwner) "삭제" else "추천",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text = if (isOwner) "삭제" else "추천",
                color = Color.White,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WriteOpenScreenPreview(){
//    WriteOpenScreen()
//    FunctionButton()
}