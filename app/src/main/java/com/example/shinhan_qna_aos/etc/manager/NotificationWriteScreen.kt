package com.example.shinhan_qna_aos.etc.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.NotificationWritingViewModel
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.etc.WriteInfo
import com.example.shinhan_qna_aos.etc.WritingContentField
import com.example.shinhan_qna_aos.etc.WritingTitleField
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun NotificationWriteScreen(viewModel: NotificationWritingViewModel) {
    val state = viewModel.state
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar("공지 작성", {})

        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WritingTitleField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    fontSize = 14.sp
                )
            }
            item {
                WritingContentField(
                    value = state.content,
                    onValueChange = viewModel::onContentChange,
                    fontSize = 14.sp
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(
                            Color.Black,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Icon(
                        painter = painterResource(lucide.images),
                        contentDescription = "사진 첨부",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "사진 첨부",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    WriteInfo()
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(
                                Color.Black,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(lucide.cloud_upload),
                            contentDescription = "작성하기",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "작성하기",
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationWritePreview(){
    val viewModel = NotificationWritingViewModel()
    NotificationWriteScreen(viewModel)
}