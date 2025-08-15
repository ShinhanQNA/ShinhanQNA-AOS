package com.example.shinhan_qna_aos.main

//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.shinhan_qna_aos.R
//import com.example.shinhan_qna_aos.Data
//import com.example.shinhan_qna_aos.main.api.PostRepository
//import com.example.shinhan_qna_aos.ui.theme.pretendard
//import com.jihan.lucide_icons.lucide

//@Composable
//fun MainScreen(postRepository: PostRepository, loginManager: Data, navController: NavController){
//
//    Box(modifier = Modifier.fillMaxSize()){
//        Column{
//            MainTopbar(navController)
//            Selectboard(postRepository, loginManager,navController)
//        }
//        Text(
//            "배너광고",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(Color.Red)
//                .align(Alignment.BottomCenter)
//        )
//    }
//}
//
//// 서브 선택
//@Composable
//fun MainTopbar(navController: NavController){
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(vertical = 18.dp, horizontal = 20.dp)
//    ){
//        Image(
//            painter = painterResource(R.drawable.biglogo),
//            modifier = Modifier.size(28.dp),
//            contentDescription = null
//        )
//        TopIcon(navController)
//    }
//}
//
//// 서브 선택
//@Composable
//fun TopIcon(navController: NavController){
//    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
//        Box(
//            modifier = Modifier
//                .border(1.dp, color = Color(0xffDFDFDF), RoundedCornerShape(10.dp))
//                .padding(6.dp)
//                .clickable { navController.navigate("writeboard")}
//        ) {
//            Icon(
//                painter = painterResource(lucide.plus),
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(16.dp)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .border(1.dp, color = Color(0xffDFDFDF), RoundedCornerShape(10.dp))
//                .padding(6.dp)
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.shape),
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(16.dp)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .border(1.dp, color = Color(0xffDFDFDF), RoundedCornerShape(10.dp))
//                .padding(6.dp)
//        ) {
//            Icon(
//                painter = painterResource(lucide.bell_ring),
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(16.dp)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .border(1.dp, color = Color(0xffDFDFDF), RoundedCornerShape(10.dp))
//                .padding(6.dp)
//        ){
//            Icon(
//                painter = painterResource(R.drawable.shield_user),
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(16.dp)
//            )
//        }
//    }
//}
//
////게시판 선택
//@Composable
//fun Selectboard(postRepository: PostRepository, loginManager: Data, navController: NavController) {
//
//    val tabData = listOf(
//        Pair("말해봐요", R.drawable.trumpet),
//        Pair("선정된 의견", R.drawable.star),
//        Pair("답변 왔어요", R.drawable.check),
//    )
//    var selectedIndex by remember { mutableStateOf(0) }
//    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
//    val interactionSource = remember { MutableInteractionSource() }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(26.dp )
//        ) {
//            tabData.forEachIndexed { idx, (title, imgRes) ->
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(48.dp)
//                        .clickable(
//                            indication = null,
//                            interactionSource = interactionSource,
//                            onClick = { selectedIndex = idx }
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    TabWithAppleEmoji(
//                        title = title,
//                        imageRes = imgRes,
//                        selected = selectedIndex == idx
//                    )
//                }
//            }
//        }
//
//        // 선 인디케이터 (기존 코드와 동일)
//        Box(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            val segmentCount = tabData.size
//            val segmentWidth = screenWidthDp / segmentCount
//            val blackBarRatio = 0.7f
//            val blackBarWidth = segmentWidth * blackBarRatio
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(Color(0xffA5A5A5), RoundedCornerShape(2.dp))
//            )
//            Box(
//                modifier = Modifier
//                    .offset(x = segmentWidth * selectedIndex + (segmentWidth - blackBarWidth) / 2)
//                    .width(blackBarWidth)
//                    .height(2.dp)
//                    .background(Color.Black, RoundedCornerShape(2.dp))
//            )
//        }
//
//        // 페이지별 본문 표시
//        when (selectedIndex) {
//            0 -> SaySomthingScreen(postRepository, loginManager ,navController =  navController)
//            1 -> SelectedOpinionsScreen()
//            2 -> AnsweredScreen()
//        }
//    }
//}
//
//@Composable
//fun TabWithAppleEmoji(
//    title: String,
//    imageRes: Int,
//    selected: Boolean
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = imageRes),
//            contentDescription = null,
//            modifier = Modifier.size(14.dp)
//        )
//        Spacer(Modifier.width(4.dp))
//        Text(
//            text = title,
//            color = if (selected) Color.Black else Color(0xFFA5A5A5),
//            style = TextStyle(
//                fontFamily = pretendard,
//                fontWeight = FontWeight.Normal,
//                fontSize = 14.sp
//            ),
//        )
//    }
//}
//
//
//@Composable
//@Preview(showBackground = true)
//fun MainScreenPreview(){
////    MainScreen()
////    MainTopbar()
//}