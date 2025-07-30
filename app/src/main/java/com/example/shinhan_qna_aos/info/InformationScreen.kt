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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.shadow
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
    var expandedGrade by remember { mutableStateOf(false) }
    var expandedMajor by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
    ) {
        // 현재 화면의 폭과 높이 구하기
        val maxWidthPx = maxWidth
        val maxHeightPx = maxHeight
        // 화면 비율(가로/세로)
        val aspectRatio = maxWidthPx / maxHeightPx

        /**
         * 반응형 파라미터 계산:
         * - Compact: 600dp 이하 (모바일)
         * - Medium: 601~840dp (태블릿)
         * - Expanded: 841dp 이상 (데스크탑)
         *
         * 화면 비율이 1보다 작으면 세로형(모바일), 1보다 크면 가로형(태블릿/데스크탑)
         */
        val (horizontalPadding, contentWidthFraction, logoSize) = when {
            maxWidthPx <= 600.dp -> { // Compact (mobile)
                val size = if (aspectRatio < 1f) 80.dp else 96.dp // 세로면 작게
                Triple(40.dp, 0.88f, size)
            }
            maxWidthPx <= 840.dp -> { // Medium (tablet)
                val size = if (aspectRatio < 1f) 112.dp else 100.dp
                Triple(32.dp, 0.7f, size)
            }
            else -> { // Expanded (desktop)
                val size = if (aspectRatio < 1.2f) 128.dp else 110.dp
                Triple(64.dp, 0.5f, size)
            }
        }

        // 실제 UI 코드 시작
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 반응형 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.biglogo),
                contentDescription = "Logo",
                modifier = Modifier.size(logoSize)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 내부 콘텐츠 (Name, StudentId, Grade, Major, Image)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth(contentWidthFraction)
            ) {
                NameField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.weight(0.45f)
                    )
                }
                MajorDropdown(
                    selected = state.major,
                    onSelectedChange = viewModel::onMajorChange,
                    options = viewModel.majorOptions,
                    expanded = expandedMajor,
                    onExpandedChange = { expandedMajor = it },
                    fontSize = 14.sp
                )
                ImageInsert(fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(36.dp))
            Request(fontSize = 14.sp)
        }
    }
}

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

// 드롭다운 필드 (공통)
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
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .widthIn(min = 180.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
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

// 개별 필드/드롭다운 컴포저블(재사용)
@Composable
fun NameField(value: String, onValueChange: (String) -> Unit, fontSize: TextUnit) =
    LabeledField("이름", fontSize) {
        PlainInputField(value, onValueChange, fontSize)
    }

@Composable
fun StudentIdField(
    value: String,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) = LabeledField("학번", fontSize, modifier) {
    PlainInputField(
        value,
        onValueChange = { newValue ->
            if (newValue.length <= 8) {
                 onValueChange(newValue)
                }
            },
        fontSize,
        keyboardType = KeyboardType.Number
    )
}

@Composable
fun GradeDropdown(
    selected: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) = DropDownField(
    "학년", selected, onSelectedChange, options, expanded, onExpandedChange, fontSize, modifier
)

@Composable
fun MajorDropdown(
    selected: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) = DropDownField(
    "학과", selected, onSelectedChange, options, expanded, onExpandedChange, fontSize, modifier
)

// 이미지 첨부 영역
@Composable
fun ImageInsert(fontSize: TextUnit){
    Column(Modifier.fillMaxWidth()) {
        Text(
            "재학 확인서 첨부(학생증, 재학증명서)",
            style = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = fontSize)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "사진 첨부", color = Color.White,
                    style = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = fontSize)
                )
            }
            Spacer(Modifier.width(10.dp))
            Text("url")
        }
    }
}

// 가입 요청 버튼
@Composable
fun Request(fontSize: TextUnit) {
    Box(
        modifier = Modifier
            .background(color = Color.Black, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(
            text = "가입요청",
            color = Color.White,
            modifier = Modifier.clickable { /* 가입 요청 로직 */ },
            style = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = fontSize)
        )
    }
}


@Composable
@Preview(showBackground = true)
fun Informationpreview(){
    val viewModel = InfoViewModel()
    InformationScreen(viewModel)
}