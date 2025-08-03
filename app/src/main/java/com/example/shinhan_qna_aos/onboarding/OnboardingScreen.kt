package com.example.shinhan_qna_aos.onboarding

import com.example.shinhan_qna_aos.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState()
    val pages = viewModel.pages

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        val maxW = maxWidth
        val maxH = maxHeight

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            userScrollEnabled = true,
        ) { page ->
            // 페이지 종류에 따라 분리
            when (page) {
                0 -> OnboardingPage1(
                    currentPage = pagerState.currentPage,
                    total = pages.size,
                    maxW = maxW
                )
                1 -> OnboardingPage2(
                    currentPage = pagerState.currentPage,
                    total = pages.size,
                    maxW = maxW,
                    maxH = maxH,
                    onFinish = onFinish
                )
            }
        }
    }
}


@Composable
fun OnboardingPage1(
    currentPage: Int,
    total: Int,
    maxW: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(horizontalAlignment = Alignment.Start){
            Row(verticalAlignment = Alignment.Bottom){
                Text(
                    "작은 의견도 편하게 ",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                )
                Text(
                    "익명의 게시글",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                )
                Text(
                    "로 남겨주세요.",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "교수님",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                )
                Text(
                    "과",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                )
                Text(
                    "학교",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                )
                Text(
                    "가 함께 귀 기울이고,",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                "여러분의 질문이나 건의에 대해 정성스럽게 답해드려요",
                color = Color.Black,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(120.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    "관리자가 게시글을 관리하고 있으니, 모두가 존중받을 수 있도록 깨끗하고 예의 바르게",
                    color = Color(0xffA5A5A5),
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 9.sp
                    ),
                )
                Row() {
                    Text(
                        "작성해 주세요. ",
                        color = Color(0xffA5A5A5),
                        style = TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                    )
                    Text(
                        "부적절한 게시글",
                        color = Color(0xffA5A5A5),
                        style = TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                    )
                    Text(
                        "을 작성할 경우 경고 또는 차단될 수 있습니다.",
                        color = Color(0xffA5A5A5),
                        style = TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp
                        ),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.android_mockup1),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        DotIndicator(size = total, current = currentPage, maxWidth = maxW)
    }
}

@Composable
fun OnboardingPage2(
    currentPage: Int,
    total: Int,
    maxW: Dp,
    maxH: Dp,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = maxW * 0.05f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = maxW * 0.05f, start = maxW * 0.05f, end = maxW * 0.05f
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .border(1.dp, Color(0xffDFDFDF), RoundedCornerShape(12.dp))
                    .size(maxW * 0.2f, maxH * 0.05f)
            ) {
                Text(
                    "확인",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }
        }
        Spacer(modifier=Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "질문에 ",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                )
                Text(
                    "답변",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp
                    ),
                )
                Text(
                    "이나 ",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                )
                Text(
                    "공지사항",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp
                    ),
                )
                Text(
                    "이 올라오면",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "푸쉬 알림",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                )
                Text(
                    "으로 바로 알려드려, 더욱 ",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                )
                Text(
                    "빠르게",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "확인",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                )
                Text(
                    "하실 수 있어요.",
                    color = Color.Black,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                )
            }
        }
        Spacer(modifier=Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.android_mockup2),
            contentDescription = null,
        )
        DotIndicator(size = total, current = currentPage, maxWidth = maxW)
    }
}

@Composable
private fun DotIndicator(size: Int, current: Int, maxWidth: Dp) {
    val selectedWidth = maxWidth * 0.15f
    val unselectedWidth = maxWidth * 0.06f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxWidth * 0.1f)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) { idx ->
            val isSelected = current == idx
            Box(
                Modifier
                    .padding(maxWidth * 0.01f)
                    .size(
                        width = if (isSelected) selectedWidth else unselectedWidth,
                        height = maxWidth * 0.013f
                    )
                    .background(
                        color = if (isSelected) Color(0xff00A5C2) else Color(0xffD9D9D9),
                        shape = RoundedCornerShape(maxWidth * 0.02f)
                    )
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun preview(){
    val viewModel = OnboardingViewModel()
    OnboardingScreen(onFinish = {}, viewModel = viewModel)
////    DotIndicator(2,1)
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .systemBarsPadding()
//    ) {
//        val maxW = maxWidth
//        val maxH = maxHeight
////        OnboardingPage1(0,2,maxW)
//        OnboardingPage2(1,2,maxW,maxH,{})
//    }
}