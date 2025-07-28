package com.example.shinhan_qna_aos.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManagerLogin(
    viewModel: ManagerLoginViewModel
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding() // 상태바+내비게이션 영역 침범 방지
            .background(Color.White)
            .padding(16.dp)
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp

        // 반응형 텍스트 스타일
        val titleStyle = if (isCompact) 32.sp else 25.sp
        val descStyle = if (isCompact) 25.sp else 20.sp
        val imageSize = if (isCompact) 60.dp else 100.dp

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ManagerId(viewModel)

            ManagerPassword(viewModel)

            ManagerLogin(modifier = Modifier)
        }
    }
}

// 아이디
@Composable
fun ManagerId(viewModel: ManagerLoginViewModel){
    Column (){
        Text(
            "아이디",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 4.dp, bottom = 4.dp)
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        // 관리자 아이디 입력
        OutlinedTextField(
            value = viewModel.state.managerId,
            onValueChange = viewModel::onAdminIdChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
}

// 비밀번호
@Composable
fun ManagerPassword(viewModel: ManagerLoginViewModel){
    Column {
        Text(
            "비밀번호",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 4.dp, bottom = 4.dp)
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        // 비밀번호 입력
        OutlinedTextField(
            value = viewModel.state.managerPassword,
            onValueChange = viewModel::onAdminPasswordChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

// 로그인 버튼
@Composable
fun ManagerLogin(modifier: Modifier = Modifier){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Box (modifier = Modifier
            .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)){
            Text(
                text = "로그인",
                color = Color.White,
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