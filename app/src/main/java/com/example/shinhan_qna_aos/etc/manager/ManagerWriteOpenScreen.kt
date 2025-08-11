package com.example.shinhan_qna_aos.etc.manager

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
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
import com.example.shinhan_qna_aos.DetailContent
import com.example.shinhan_qna_aos.LikeFlagBan
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.TopBar
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

@Composable
fun ManagerWriteOpenScreen(
    isNotice: Boolean = true // true면 공지사항, false면 일반글
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 50.dp)
        ) {
            TopBar(null, {})
            DetailContent(title = "", content = "")

            if (!isNotice) {
                Spacer(modifier = Modifier.height(16.dp))
                LikeFlagBan(likeCount = 45, flagsCount = 10, banCount = 2)
            }

            Spacer(modifier = Modifier.height(32.dp))
            ManagerFunctionButton(isNotice)
        }
        // 배너광고는 항상 하단 고정
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

@Composable
fun ManagerFunctionButton(isNotice: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ManagerButton(
            icon = if (isNotice) lucide.trash else lucide.list,
            label = if (isNotice) "삭제" else "검토",
            background = Color(0xffFC4F4F)
        )

        Spacer(modifier = Modifier.width(16.dp))

        ManagerButton(
            icon = if (isNotice) R.drawable.square_pen else lucide.flag,
            label = if (isNotice) "수정" else "경고",
            background = if (isNotice) Color.Black else Color(0xffFF9F43)
        )
    }
}

@Composable
fun ManagerButton(icon: Int, label: String, background: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
        Text(
            text = label,
            color = Color.White,
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WriteOpenScreenPreview(){
    ManagerWriteOpenScreen()
//    FunctionButton()
}