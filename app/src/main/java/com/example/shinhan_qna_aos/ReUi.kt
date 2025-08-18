package com.example.shinhan_qna_aos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide
//
//// 데이터 클래스 임의
//data class TitleContentLike(
//    val title: String,
//    val content: String,
//    val likeCount: Int,
//    val flagsCount: Int = 0,
//    val banCount: Int = 0,
//    val responseState: String = "응답 상태"
//)
//
//data class TitleContent(val title: String, val content: String)
//data class SelectData(val year: Int,val month: Int, val week: Int, val count: Int, val responseState: String = "응답 상태")
//data class StringData(val content:String)
//data class TitleYearData(val name: String,val studentid: String, val grade: String,val major: String,val year: Int,val month: Int,val day: Int)

val TitleTextStyle = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    color = Color.Black
)

val ContentTextStyle = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = Color(0xffA5A5A5)
)

// 공통 레이블 + 필드
@Composable
fun LabeledField(
    label: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = fontSize
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

// 일반 입력 필드
@Composable
fun PlainInputField(
    value: String,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = fontSize,
            fontFamily = pretendard,
            lineHeight = fontSize
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}


// 게시글 + 좋아요/신고/차단 + 관리자 드롭다운
@Composable
fun TitleContentLikeButton(
    title: String,
    content: String,
    likeCount: Int,
    flagsCount: Int = 0,
    banCount: Int = 0,
    isAdmin: Boolean = false,
    responseState: String = "응답 상태",
    onResponseStateChange: (String) -> Unit = {},
    onClick:()->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, style = TitleTextStyle)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
                style = ContentTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoIconCount(lucide.thumbs, "좋아요 표시", likeCount, Color.Black, 16)
                if (isAdmin) {
                    InfoIconCount(R.drawable.flag, "신고 표시", flagsCount, Color(0xffFF9F43), 16)
                    InfoIconCount(lucide.ban, "차단 표시", banCount, Color(0xffFC4F4F), 16)
                }
            }
        }
        if (isAdmin) {
            ManagerDropDown(responseState, onResponseStateChange = onResponseStateChange)
        }
    }
}

//// 일반 게시글 컴포저블
//@Composable
//fun TitleContentButton(title: String, content: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = Color.White)
//            .padding(horizontal = 20.dp, vertical = 16.dp)
//    ) {
//        Text(title, style = TitleTextStyle)
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(content, style = ContentTextStyle, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)
//    }
//}
//// 주차 선정 의견
//@Composable
//fun SelectDataButton(
//    year: Int,
//    month: Int,
//    week: Int,
//    count: Int,
//    isAdmin: Boolean = false,
//    responseState: String = "응답 상태",
//    onResponseStateChange: (String) -> Unit = {}
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(horizontal = 20.dp, vertical = 16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column {
//            Text("${year}년 ${month}월 ${week}주차", style = TitleTextStyle)
//            Spacer(Modifier.height(8.dp))
//            Text("의견 $count 개", style = ContentTextStyle, maxLines = 1)
//            Spacer(Modifier.height(8.dp))
//            Box(
//                modifier = Modifier
//                    .background(Color(0xffFF9F43), RoundedCornerShape(20.dp))
//                    .padding(horizontal = 12.dp, vertical = 2.dp)
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(R.drawable.ellipse),
//                        contentDescription = "상태 아이콘",
//                        modifier = Modifier.size(10.dp)
//                    )
//                    Spacer(Modifier.width(4.dp))
//                    Text("대기", style = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Bold, fontSize = 12.sp, color =  Color.White))
//                }
//            }
//        }
//        if (isAdmin) {
//            ManagerDropDown(responseState, onResponseStateChange = onResponseStateChange)
//        }
//    }
//}

// 관리자용 응답 상태 드롭다운
@Composable
fun ManagerDropDown(
    responseState: String,
    responseOptions: List<String> = listOf("대기", "응답중", "응답 완료"),
    onResponseStateChange: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }  // 드롭다운 확장 상태 내부 관리
    Box {
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (responseState.isBlank() || responseState == "응답 상태") "응답 상태" else responseState,
                fontSize = 13.sp,
                fontFamily = pretendard,
                color =  Color.Black
            )
            Icon(
                painter = painterResource(lucide.chevron_down),
                contentDescription = "응답 상태 선택",
                modifier = Modifier.size(18.dp),
                tint = Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            responseOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontFamily = pretendard, fontSize = 14.sp) },
                    onClick = {
                        onResponseStateChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// 탑바
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title : String?,
    onNavigationClick: () -> Unit
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)) {
                Text(
                    text = title ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    maxLines = 1,
                )
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = lucide.chevron_left),
                contentDescription = "Navigation Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigationClick() }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White),
        colors = TopAppBarDefaults.topAppBarColors(Color.White)
    )
}

// 상세보기 [제목 + 내용]
@Composable
fun DetailContent(title: String, content: String) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            title,
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            content,
            color = Color.Black,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            lineHeight = 28.sp
        )
    }
}


//// 개인정보처리방침, 이용약관, 법적고지 텍스트 영역 컴포저블
//@Composable
//fun Caution() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
//        horizontalArrangement = Arrangement.Center,
//    ) {
//        val items = listOf("개인정보처리방침", "이용약관", "법적고지")
//        items.forEachIndexed { index, item ->
//            Text(
//                text = item,
//                color = Color(0xffa5a5a5),
//                style = TextStyle(
//                    fontFamily = pretendard,
//                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
//                    fontSize = 12.sp
//                ),
//                modifier = Modifier.clickable { /* TODO: 클릭 처리 */ }
//            )
//            if (index < items.lastIndex) Spacer(modifier = Modifier.width(8.dp))
//        }
//    }
//}
//// 제목이 학생 이름, 학번, 학년, 전공 + 날짜
//@Composable
//fun TitleYearButton(name: String, studentid:String, grade:String ,major:String,year: Int, month:Int, day:Int) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = Color.White)
//            .padding(horizontal = 20.dp, vertical = 16.dp)
//    ) {
//        Text(
//            "${name}/${studentid}/${grade}/${major}/",
//            color = Color.Black,
//            style = TextStyle(
//                fontFamily = pretendard,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp
//            ),
//            overflow = TextOverflow.Ellipsis
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            "${year}년 ${month}월 ${day}일",
//            color = Color(0xffA5A5A5),
//            style = TextStyle(
//                fontFamily = pretendard,
//                fontWeight = FontWeight.Normal,
//                fontSize = 14.sp
//            ),
//        )
//    }
//}
//
//// 관리자 학생 정보 단일 필드 컴포저블
//@Composable
//fun ManagerStudentInfo(title: String, info: String, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text(
//            title,
//            style = TextStyle(
//                color = Color.Black,
//                fontFamily = pretendard,
//                fontWeight = FontWeight.Normal,
//                fontSize = 14.sp
//            )
//        )
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .border(1.dp, Color(0xffdfdfdf), RoundedCornerShape(10.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp)
//        ) {
//            Text(
//                info,
//                style = TextStyle(
//                    fontFamily = pretendard,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 15.sp,
//                    color = Color.Black
//                ),
//            )
//        }
//    }
//}
// 좋아요, 신고, 차단 아이콘과 카운트 컴포저블
@Composable
fun LikeFlagBan(likeCount: Int, flagsCount: Int, banCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InfoIconCount(lucide.thumbs, "좋아요 표시", likeCount, Color.Black, 14)
        InfoIconCount(R.drawable.flag, "신고 표시", flagsCount, Color(0xffFF9F43), 14)
        InfoIconCount(lucide.ban, "차단 표시", banCount, Color(0xffFC4F4F), 14)
    }
}

// 공통 아이콘+숫자 표시 컴포저블
@Composable
fun InfoIconCount(
    icon: Int,
    desc: String,
    count: Int,
    color: Color,
    fontSize: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = desc,
            modifier = Modifier.size(16.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            count.toString(),
            color = color,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = fontSize.sp
            ),
        )
    }
}