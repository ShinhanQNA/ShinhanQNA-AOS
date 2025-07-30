package com.example.shinhan_qna_aos.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.info.LabeledField
import com.example.shinhan_qna_aos.info.PlainInputField
import com.example.shinhan_qna_aos.ui.theme.pretendard

@Composable
fun ManagerLogin(
    viewModel: ManagerLoginViewModel
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // 상태바+내비게이션 영역 침범 방지
            .background(Color.White)
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp

        // 반응형 텍스트 스타일
        val titleStyle = if (isCompact) 32.sp else 25.sp
        val descStyle = if (isCompact) 25.sp else 20.sp
        val imageSize = if (isCompact) 60.dp else 100.dp

        Column (
            modifier = Modifier.fillMaxSize().padding(horizontal = 58.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ManagerId(
                value = viewModel.state.managerId,
                onValueChange = viewModel::onAdminIdChange,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            ManagerPassword(
                value = viewModel.state.managerPassword,
                onValueChange = viewModel::onAdminPasswordChange,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(36.dp))
            ManagerLogin(modifier = Modifier)
        }
    }
}

// 아이디
@Composable
fun ManagerId(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit) =
    LabeledField("아이디", fontSize) {
        PlainInputField(value, onValueChange, fontSize)
    }


// 비밀번호
@Composable
fun ManagerPassword(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit) =
    LabeledField("비밀번호", fontSize) {
        PlainInputField(value, onValueChange, fontSize, keyboardType = KeyboardType.Password)
    }

// 로그인 버튼
@Composable
fun ManagerLogin(modifier: Modifier = Modifier){
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ){
        Box (modifier = Modifier
            .background(color = Color.Black, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp)){
            Text(
                text = "로그인",
                color = Color.White,
                style = TextStyle(
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                modifier = Modifier.clickable { /** 로그인 요청 **/ }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Managerpreview(){
    val viewModel=ManagerLoginViewModel()
    ManagerLogin(viewModel)
}