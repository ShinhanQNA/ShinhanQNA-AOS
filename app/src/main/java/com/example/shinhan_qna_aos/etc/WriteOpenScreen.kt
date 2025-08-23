package com.example.shinhan_qna_aos.etc

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.shinhan_qna_aos.etc.api.WriteData
import com.example.shinhan_qna_aos.etc.api.WriteRepository
import com.example.shinhan_qna_aos.etc.api.WritingViewModel
import com.example.shinhan_qna_aos.main.api.PostRepository
import com.example.shinhan_qna_aos.main.api.PostViewModel
import com.example.shinhan_qna_aos.main.warningStatusToBanCount
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun WriteOpenScreen(
    navController: NavController,
    postRepository: PostRepository,
    writeRepository: WriteRepository,
    data: Data,
    postId: String,
) {
    val context = LocalContext.current
    val postViewModel: PostViewModel =
        viewModel(factory = SimpleViewModelFactory { PostViewModel(postRepository) })
    val writingViewModel: WritingViewModel =
        viewModel(factory = SimpleViewModelFactory { WritingViewModel(writeRepository) })

    val postDetail = postViewModel.selectedPost
    val uiState = writingViewModel.state   // 현재 화면 상태

    // 이미지 선택 런처
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { writingViewModel.onImageChange(context, it) }
    }

    // 첫 진입시 상세 데이터 불러오기
    LaunchedEffect(postId) {
        postViewModel.loadPostDetail(postId)
    }

    postDetail?.let { detail ->
        val isOwner = data.userEmail == detail.writerEmail

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = 50.dp)
            ) {
                TopBar(if (uiState.isEditMode) "게시글 수정" else null) { navController.navigate("main?selectedTab=0") {popUpTo("writeOpen/$postId"){inclusive=true} }}

                Spacer(modifier = Modifier.height(16.dp))

                //  수정 모드
                if (uiState.isEditMode && isOwner) {
                    EditPostContent(
                        uiState = uiState,
                        writingViewModel = writingViewModel,
                        launcher = launcher,
                        context = context,
                        postId = postId,
                        navController = navController,
                        postViewModel = postViewModel
                    )
                } else {
                    //  읽기 모드
                    Log.d("Compose", "postDetail.imagePath: ${postDetail.imagePath}")
                    LazyColumn {
                        item {
                            DetailContent(detail.title, detail.content, detail.imagePath)
                            Spacer(modifier = Modifier.height(16.dp))
                            LikeFlagBan(detail.likes, detail.reportCount, warningStatusToBanCount(detail.warningStatus).toInt(), data)
                            Spacer(modifier = Modifier.height(36.dp))
                            if (data.isAdmin) {
                                ManagerFunctionButton(data.isNotice)
                            } else {
                                if(isOwner){
                                    EditDeleteButton(
                                        onDeleteClick = {
                                            Log.d("Compose", "삭제 버튼 클릭됨")
                                            postViewModel.deletePost(postId.toInt())
                                            navController.popBackStack()
                                        },
                                        onEditClick = { writingViewModel.enterEditMode(detail, context) }
                                    )
                                }else{
                                    FlagLikeButton(
                                        onFlagClick = {
                                            Log.d("Compose", "신고 버튼 클릭됨")
                                            postViewModel.flagPost(postId.toInt(), "",context)
                                            postViewModel.loadPostDetail(postId)
                                        },
                                        onLikeClick = {
                                            Log.d("Compose", "좋아요 버튼 클릭됨")
                                            postViewModel.toggleLike(postId.toInt())
                                            postViewModel.loadPostDetail(postId)
                                        },
                                    )
                                }
                                Spacer(modifier = Modifier.height(36.dp))
                            }
                        }
                    }
                }
            }

            // 하단 배너 광고
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
fun EditPostContent(
    uiState: WriteData,
    writingViewModel: WritingViewModel,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    context: Context,
    postId: String,
    navController: NavController,
    postViewModel: PostViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        Column {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    WritingTitleField(
                        value = uiState.title,
                        onValueChange = writingViewModel::onTitleChange,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    WritingContentField(
                        value = uiState.content,
                        onValueChange = writingViewModel::onContentChange,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .background(Color.Black, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { launcher.launch("image/*") }
                        ) {
                            Icon(
                                painter = painterResource(lucide.images),
                                contentDescription = "사진 첨부",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Text("사진 첨부", color = Color.White, fontSize = 14.sp)
                        }
                        Text(
                            uiState.imageUri?.lastPathSegment ?: "",
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    WriteInfo()
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }

        //  수정 완료 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .clickable {
                    writingViewModel.updatePost(
                        postId = postId,
                        onSuccess = {
                            postViewModel.loadPostDetail(postId) // 상세 조회 로드
                            postViewModel.loadPosts() // 전체 조회 로드
                            navController.navigate("writeOpen/$postId")
                        },
                        onError = {
                            Toast.makeText(context, "게시글 수정 실패", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
        ) {
            Icon(
                painter = painterResource(lucide.cloud_upload),
                contentDescription = "작성하기",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text("작성하기", color = Color.White, fontSize = 14.sp)
        }
    }
}


@Composable
fun FlagLikeButton( // 작성자가 아닐때
    onFlagClick: () -> Unit,
    onLikeClick: () -> Unit,
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
                .clickable { onFlagClick() }  // 신고 버튼 눌리면 콜백 호출
        ) {
            Icon(
                painter = painterResource( R.drawable.flag),
                contentDescription =  "신고",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text = "신고",
                color = Color.White,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 두 번째 버튼: 추천
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(Color.Black, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { onLikeClick() }
        ) {
            Icon(
                painter = painterResource(lucide.thumbs),
                contentDescription = "추천",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text =  "추천" ,
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

@Composable
fun EditDeleteButton( // 작성자
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        // 첫 번째 버튼: 수정
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(Color(0xffFF9F43), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { onEditClick() }  // 수정 버튼 눌리면 콜백 호출
        ) {
            Icon(
                painter = painterResource( R.drawable.square_pen ),
                contentDescription = "수정",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text = "수정",
                color = Color.White,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 두 번째 버튼: 삭제
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(Color(0xffFC4F4F), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { onDeleteClick() }
        ) {
            Icon(
                painter = painterResource( lucide.trash ),
                contentDescription = "삭제/추천",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Text(
                text = "삭제",
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