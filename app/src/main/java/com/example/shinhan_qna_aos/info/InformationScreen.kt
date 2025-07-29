package com.example.shinhan_qna_aos.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinhan_qna_aos.R
import com.example.shinhan_qna_aos.ui.theme.pretendard
import com.jihan.lucide_icons.lucide

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
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)){
                NameField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                ) {
                    StudentIdField(
                        value = state.studentId,
                        onValueChange = viewModel::onStudentIdChange,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    GradeDropdown(
                        selected = state.grade,
                        onSelectedChange = viewModel::onGradeChange,
                        options = viewModel.gradeOptions,
                        expanded = expandedGrade,
                        onExpandedChange = { expandedGrade = it },
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.4f)
                    )
                }
                MajorDropdown(
                    selected = state.major,
                    onSelectedChange = viewModel::onMajorChange,
                    options = viewModel.majorOptions,
                    expanded = expandedMajor,
                    onExpandedChange = { expandedMajor = it },
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                ImageInsert(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(36.dp))

            Request(
                modifier = Modifier.fillMaxWidth(0.8f),
                fontSize = 14.sp
            )
        }
    }
}

// 공통 레이블 박스
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

// 공통 입력 필드
@Composable
fun PlainInputField(
    value: String,
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
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = fontSize,
                fontFamily = pretendard,
                lineHeight = fontSize,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// 드롭다운 박스
@Composable
fun DropDownField(
    label: String,
    selected: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    LabeledField(label, fontSize, modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFdfdfdf), RoundedCornerShape(10.dp))
                .clickable { onExpandedChange(!expanded) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected,
                    fontSize = fontSize,
                    fontFamily = pretendard,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(lucide.chevron_down),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "드롭다운 열기",
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = fontSize, fontFamily = pretendard) },
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

// 이름 필드
@Composable
fun NameField(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit, modifier: Modifier = Modifier) {
    LabeledField("이름", fontSize, modifier) {
        PlainInputField(
            value = value,
            onValueChange = onValueChange,
            fontSize = fontSize
        )
    }
}

// 학번 필드
@Composable
fun StudentIdField(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit, modifier: Modifier = Modifier) {
    LabeledField("학번", fontSize, modifier) {
        PlainInputField(
            value = value,
            onValueChange = onValueChange,
            fontSize = fontSize,
            keyboardType = KeyboardType.Number
        )
    }
}

// 학년 드롭다운
@Composable
fun GradeDropdown(
    selected: String, onSelectedChange: (String) -> Unit,
    options: List<String>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit, modifier: Modifier = Modifier
) {
    DropDownField(
        label = "학년",
        selected = selected,
        onSelectedChange = onSelectedChange,
        options = options,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        fontSize = fontSize,
        modifier = modifier
    )
}

// 학과 드롭다운
@Composable
fun MajorDropdown(
    selected: String, onSelectedChange: (String) -> Unit,
    options: List<String>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit, modifier: Modifier = Modifier
) {
    DropDownField(
        label = "학과",
        selected = selected,
        onSelectedChange = onSelectedChange,
        options = options,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        fontSize = fontSize,
        modifier = modifier
    )
}

// 이미지 삽입 + 여기 지금 비트맵 압축 필요함
@Composable
fun ImageInsert(modifier: Modifier = Modifier,fontSize: TextUnit){
    Column(modifier = modifier.fillMaxWidth()) {
        Text("재학 확인서 첨부(학생증, 재학증명서)",
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = fontSize
            ),
        )
        Spacer(modifier=Modifier.height(8.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "사진 첨부",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = fontSize
                    )
                )
            }
            Spacer(modifier=Modifier.width(10.dp))
            Text(text = "url")
        }
    }
}
// 가입 요청
@Composable
fun Request(modifier: Modifier = Modifier,fontSize: TextUnit) {
    Box(
        modifier = Modifier
            .background(color = Color.Black, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(
            text = "가입요청",
            color = Color.White,
            modifier = Modifier.clickable { /** 가입 요청 **/ },
            style = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = fontSize
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Informationpreview(){
    val viewModel = InfoViewModel()
    InformationScreen(viewModel)
}