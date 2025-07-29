package com.example.shinhan_qna_aos.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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

            val pageData = pages[page]

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                if (pagerState.currentPage == pages.lastIndex) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = maxW * 0.05f, start = maxW * 0.05f, end = maxW * 0.05f),
                        contentAlignment = Alignment.TopEnd
                    ){
                        Button(
                            onClick = onFinish,
                            colors = ButtonDefaults.buttonColors(Color.White),
                            modifier = Modifier.border(1.dp,Color(0xffDFDFDF),RoundedCornerShape(12.dp))
                                .size(maxW*0.2f,maxH*0.05f)
                        ) {
                            Text("확인",
                                color = Color.Black
                                )
                        }
                    }
                }

                Image(
                    painter = painterResource(id = pageData.imageRes),
                    contentDescription = null,
                )

                DotIndicator(
                    size = pages.size,
                    current = pagerState.currentPage,
                    maxWidth = maxW
                )
            }
        }
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
//    DotIndicator(2,1)
}