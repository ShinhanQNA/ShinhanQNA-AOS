package com.example.shinhan_qna_aos.etc

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.shinhan_qna_aos.PlainInputField
import com.example.shinhan_qna_aos.etc.api.WriteRepository
import com.example.shinhan_qna_aos.etc.api.WritingViewModel
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

//@Composable
//fun WriteOpenScreen (
//    navController: NavController,
//    postRepository: PostRepository,
//    data: Data
//) {
//    val postViewModel: PostViewModel = viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository,data) })
//
//    val postDetail = postViewModel.selectedPost
//
//    // 처음 진입 시 데이터 로드
//    LaunchedEffect(Unit) {
//        postViewModel.loadPostDetail()
//    }
//
//    postDetail?.let { post_detail ->
//        val isOwner = data.userEmail == post_detail.email
//        Box {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//                    .padding(bottom = 50.dp)
//            ) {
//                TopBar(null) { navController.popBackStack() }
//                DetailContent(post_detail.title, post_detail.content)
//                Spacer(modifier = Modifier.height(16.dp))
//                LikeFlagBan(
//                    post_detail.likes,
//                     0, // 나중에 API
//                     34, // 나중에 API
//                     data
//                )
//                Spacer(modifier = Modifier.height(36.dp))
//                if (data.isAdmin) {
//                    ManagerFunctionButton(data.isNotice)
//                } else {
//                    FunctionButton(isOwner)
//                }
//            }
//            Text(
//                "배너광고",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .background(Color.Red)
//                    .align(Alignment.BottomCenter)
//            )
//        }
//    }
//}

@Composable
fun WriteOpenScreen (
    navController: NavController,
    postRepository: PostRepository,
    writeRepository: WriteRepository,
    data: Data
) {
    val context = LocalContext.current
    val postViewModel: PostViewModel =
        viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository, data) })
    val writingViewModel: WritingViewModel =
        viewModel(factory = SimpleViewModelFactory { WritingViewModel(writeRepository) })
    val postDetail = postViewModel.selectedPost

    // 수정 모드 상태 추가
    var isEditMode by remember { mutableStateOf(false) }

    // 수정용 상태 변수 - postDetail이 바뀔 때 초기값 세팅
    var title by remember(postDetail) { mutableStateOf(postDetail?.title ?: "") }
    var content by remember(postDetail) { mutableStateOf(postDetail?.content ?: "") }

    // 처음 진입 시 상세 데이터 불러오기
    LaunchedEffect(Unit) {
        postViewModel.loadPostDetail()
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

                Spacer(modifier = Modifier.height(16.dp))
                // 수정 모드
                if (isEditMode && isOwner) {
                    // 일반 입력 필드로 제목 입력
                    PlainInputField(
                        value = title,
                        onValueChange = { title = it },
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // 일반 입력 필드로 내용 입력 (여러 줄 입력하려면 maxLines 없이 사용)
                    PlainInputField(
                        value = content,
                        onValueChange = { content = it },
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 저장 및 취소 버튼 Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { isEditMode = false }) {
                            Text("취소")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            // 수정 API 호출
                            writingViewModel.updatePost(
                                title = title,
                                content = content,
                                onSuccess = {
                                    isEditMode = false
                                    postViewModel.loadPostDetail() // 상세 데이터 갱신
                                    postViewModel.loadPosts()      // 전체 목록 갱신
                                    navController.popBackStack()
                                },
                                category = "없음",
                                onError = { Toast.makeText(context,"게시글 수정이 실패하였습니다",Toast.LENGTH_SHORT).show()}
                            )
                        }) {
                            Text("저장")
                        }
                    }
                } else {
                    // 수정 모드가 아닐 때는 기존 내용 보여주기
                    DetailContent(post_detail.title, post_detail.content)
                    Spacer(modifier = Modifier.height(16.dp))
                    LikeFlagBan(post_detail.likes, 0, 34, data)
                    Spacer(modifier = Modifier.height(36.dp))

                    if (data.isAdmin) {
                        ManagerFunctionButton(data.isNotice)
                    } else {
                        FunctionButton(isOwner, onEditClick = { if (isOwner) isEditMode = true })
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
}

//@Composable
//fun FunctionButton(
//    isOwner: Boolean
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp),
//        horizontalArrangement = Arrangement.End,
//    ) {
//        // 첫 번째 버튼
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(6.dp),
//            modifier = Modifier
//                .background(Color(0xffFF9F43), RoundedCornerShape(12.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//        ) {
//            Icon(
//                painter = painterResource(
//                    if (isOwner) R.drawable.square_pen else R.drawable.flag
//                ),
//                contentDescription = if (isOwner) "수정" else "신고",
//                modifier = Modifier.size(20.dp),
//                tint = Color.White
//            )
//            Text(
//                text = if (isOwner) "수정" else "신고",
//                color = Color.White,
//                style = TextStyle(
//                    fontFamily = pretendard,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 14.sp
//                ),
//            )
//        }
//
//        Spacer(modifier = Modifier.width(16.dp))
//
//        // 두 번째 버튼
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(6.dp),
//            modifier = Modifier
//                .background(
//                    if (isOwner) Color(0xffFC4F4F) else Color.Black,
//                    RoundedCornerShape(12.dp)
//                )
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//        ) {
//            Icon(
//                painter = painterResource(
//                    if (isOwner) lucide.trash else lucide.thumbs
//                ),
//                contentDescription = if (isOwner) "삭제" else "추천",
//                modifier = Modifier.size(20.dp),
//                tint = Color.White
//            )
//            Text(
//                text = if (isOwner) "삭제" else "추천",
//                color = Color.White,
//                style = TextStyle(
//                    fontFamily = pretendard,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 14.sp
//                ),
//            )
//        }
//    }
//}

@Composable
fun FunctionButton(
    isOwner: Boolean,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        // 첫 번째 버튼: 수정 or 신고
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(Color(0xffFF9F43), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { if (isOwner) onEditClick() }  // 수정 버튼 눌리면 콜백 호출
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

        // 두 번째 버튼: 삭제 or 추천 (기존대로)
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