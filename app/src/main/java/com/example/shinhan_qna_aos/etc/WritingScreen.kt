package com.example.shinhan_qna_aos.etc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun WritingScreen(viewModel: WritingViewModel){
    val state = viewModel.state
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            TopBar("게시글 작성", {})

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

        Text(
            "배너광고",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Red)
                .align(Alignment.BottomCenter),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

// 제목 입력 필드
@Composable
fun WritingTitleField(
    value: String?,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (value.isNullOrEmpty()) {
            Text(
                text = "제목을 입력해주세요.",
                color = Color(0xffDFDFDF),
                fontSize = fontSize,
                fontFamily = pretendard,
                lineHeight = fontSize,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        BasicTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = fontSize,
                fontFamily = pretendard,
                lineHeight = fontSize
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// 본문 입력 필드
@Composable
fun WritingContentField(
    value: String?,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
    ) {
        if (value.isNullOrEmpty()) {
            Text(
                text = "정확한 전달을 위해 교수님 성함 혹은 과목명을 정확하게 기재해주세요.",
                color = Color(0xffDFDFDF),
                fontSize = fontSize,
                fontFamily = pretendard,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
            )
        }
        BasicTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = fontSize,
                fontFamily = pretendard,
                lineHeight = fontSize
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun WriteInfo(){
    Text("주의사항 및 안내사항\n" +
            "건전하고 유익한 커뮤니티 환경을 위해 다음 사항을 준수해주세요.\n" +
            "\n" +
            "특정 개인이나 단체에 대한 비방, 욕설, 혐오 발언 등 다른 사용자에게 불쾌감을 주는 게시물은 금지됩니다.\n" +
            "\n" +
            "본인 및 타인의 개인정보(이름, 연락처, 학번, 주소 등)를 무단으로 게시할 경우, 법적인 문제가 발생할 수 있습니다.\n" +
            "\n" +
            "확인되지 않은 허위사실을 유포하거나 타인의 명예를 훼손하는 내용은 작성할 수 없습니다.\n" +
            "\n" +
            "상업적 목적의 광고, 홍보성 게시물 및 도배성 게시물은 제재 대상이 될 수 있습니다.\n" +
            "\n" +
            "위의 사항에 위배되는 게시글은 사전 통보 없이 삭제될 수 있으며, 서비스 이용이 제한될 수 있습니다.\n" +
            "\n" +
            "게시글에 대한 법적 책임은 전적으로 작성자 본인에게 있습니다.",
        color = Color(0xffA5A5A5),
        style = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun WritinScreenPreview(){
    val viewModel = WritingViewModel()
    WritingScreen(viewModel)
}