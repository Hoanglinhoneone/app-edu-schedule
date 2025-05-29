package com.hiendao.eduschedule.ui.screen.add

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

@SuppressLint("NewApi")
@Composable
fun MultiDatePickerDialog(
    selectedDates: Set<LocalDate>,
    onDatesSelected: (Set<LocalDate>) -> Unit,
    onDismiss: () -> Unit
) {
    var currentSelectedDates by remember { mutableStateOf(selectedDates) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onDatesSelected(currentSelectedDates)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        title = { Text("Chọn ngày lặp lại") },
        text = {
            Column {
                val yearMonth = remember { YearMonth.now() }
                val daysInMonth = yearMonth.lengthOfMonth()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7)
                ) {
                    items(daysInMonth) { day ->
                        val date = yearMonth.atDay(day + 1)
                        val isSelected = date in currentSelectedDates

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    if (isSelected) Color.Blue else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    currentSelectedDates = if (isSelected) {
                                        currentSelectedDates - date
                                    } else {
                                        currentSelectedDates + date
                                    }
                                },
                            contentAlignment = Center
                        ) {
                            Text(text = "${day + 1}")
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TestPreview(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(true) }
    var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }

    Button(onClick = { showDialog = true }) {
        Text("Chọn ngày")
    }

    if (showDialog) {
        MultiDatePickerDialog(
            selectedDates = selectedDates,
            onDatesSelected = { dates -> selectedDates = dates },
            onDismiss = { showDialog = false }
        )
    }
}
