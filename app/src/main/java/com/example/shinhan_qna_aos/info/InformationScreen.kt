package com.example.shinhan_qna_aos.info

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R

@Composable
fun InformationScreen(
    viewModel: InfoViewModel
) {
    val state = viewModel.state
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
            .padding(16.dp)
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 360.dp
        val bodyFontSize = if (isCompact) 10.sp else 16.sp
        val titleFontSize = if (isCompact) 16.sp else 20.sp
        val imageSize = if (isCompact) 60.dp else 100.dp

        // 드롭다운 확장 상태
        var expandedGrade by remember { mutableStateOf(false) }
        var expandedMajor by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.biglogo),
                contentDescription = "Logo",
                modifier = Modifier.size(imageSize)
            )
            Spacer(modifier = Modifier.height(24.dp))

            NameField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                fontSize = titleFontSize,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalAlignment = Alignment.Bottom
            ) {
                StudentIdField(
                    value = state.studentId,
                    onValueChange = viewModel::onStudentIdChange,
                    fontSize = titleFontSize,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                GradeDropdown(
                    selected = state.grade,
                    onSelectedChange = viewModel::onGradeChange,
                    options = viewModel.gradeOptions,
                    expanded = expandedGrade,
                    onExpandedChange = { expandedGrade = it },
                    fontSize = titleFontSize,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            MajorDropdown(
                selected = state.major,
                onSelectedChange = viewModel::onMajorChange,
                options = viewModel.majorOptions,
                expanded = expandedMajor,
                onExpandedChange = { expandedMajor = it },
                fontSize = titleFontSize,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            ImageInsert(
                modifier = Modifier.fillMaxWidth(0.8f),
                fontSize = bodyFontSize
            )
            Spacer(modifier = Modifier.height(12.dp))
            Request(modifier = Modifier)
        }
    }
}
// 이름 입력 컴포저블
@Composable
fun NameField(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("이름", fontSize = fontSize, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
}

// 학번 입력 컴포저블
@Composable
fun StudentIdField(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("학번", fontSize = fontSize, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

// 학년 드롭다운
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeDropdown(
    selected: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("학년", fontSize = fontSize, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selected,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = fontSize) },
                        onClick = {
                            onSelectedChange(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

// 학과 드롭다운
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MajorDropdown(
    selected: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("학과", fontSize = fontSize, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selected,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = fontSize) },
                        onClick = {
                            onSelectedChange(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

// 이미지 삽입 + 여기 지금 비트맵 압축 필요함
@Composable
fun ImageInsert(modifier: Modifier = Modifier,fontSize: TextUnit){
    Column(modifier = modifier) {
        Text("재학 확인서 첨부(학생증, 재학증명서)", fontSize = fontSize, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.background(
                    color = Color.Black,
                    shape = RoundedCornerShape(20.dp)
                ).padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "사진 첨부",
                    color = Color.White
                )
            }
            Text(text = "url")
        }
    }
}
// 가입 요청
@Composable
fun Request(modifier: Modifier = Modifier){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Box (modifier = Modifier.background(color = Color.Black, shape = RoundedCornerShape(20.dp)) .padding(horizontal = 20.dp, vertical = 10.dp)){
            Text(
                text = "가입요청",
                color = Color.White,
                modifier = Modifier.clickable { /** 가입 요청 **/ }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Informationpreview(){
    val viewModel = InfoViewModel()
    InformationScreen(viewModel)
}