package com.example.shinhan_qna_aos.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shinhan_qna_aos.SelectData
import com.example.shinhan_qna_aos.SelectDataButton

@Composable
fun SelectedOpinionsScreen() {
    val dataList = listOf( // 임의 값
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9 ),
        SelectData(2003,3,2,9 ),
        SelectData(2003,3,2,9),
        SelectData(2003,3,2,9)
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)){
        items(dataList) { data ->
            SelectDataButton(
                year = data.year,
                month = data.month,
                week = data.week,
                count = data.count
            )
            Divider()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SelectedOpinionsPreview(){
    SelectedOpinionsScreen()
}