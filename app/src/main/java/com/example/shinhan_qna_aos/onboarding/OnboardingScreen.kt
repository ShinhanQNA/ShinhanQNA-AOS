package com.example.shinhan_qna_aos.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding() // 상태바 + 내비게이션바 침범 방지
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val maxW = maxWidth
            val isCompact = maxW < 360.dp
            val horizontalPadding = if (isCompact) 16.dp else 32.dp
//            val titleStyle = if (isCompact) MaterialTheme.typography.h6 else MaterialTheme.typography.h4
//            val descStyle = if (isCompact) MaterialTheme.typography.body2 else MaterialTheme.typography.body1
            val imageSize = if (isCompact) 120.dp else 200.dp

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                userScrollEnabled = true,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = pages[page].title)
                    Spacer(Modifier.height(8.dp))
                    Text(text = pages[page].description)
                    Spacer(Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = pages[page].imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(imageSize)
                    )
                }
            }

            // 닷 인디케이터 (하단 중앙 위치)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp), // 버튼 위에 충분한 공간 확보
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { idx ->
                    val selected = pagerState.currentPage == idx
                    Box(
                        Modifier
                            .padding(4.dp)
                            .size(if (selected) 12.dp else 8.dp)
                            .background(
                                if (selected) Color.Black else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }

            // 마지막 페이지에만 "시작하기" 버튼 (하단 우측에 배치)
            if (pagerState.currentPage == pages.lastIndex) {
                Button(
                    onClick = onFinish,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 24.dp)
                ) {
                    Text("시작하기")
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun preview(){
    val viewModel = OnboardingViewModel()
    OnboardingScreen(onFinish = {}, viewModel = viewModel)
}