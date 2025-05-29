package com.hiendao.eduschedule.ui.screen.add

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AddNoteScreen(
    tintIcon: Color,
    modifier: Modifier = Modifier,
    currentNotes: String = ""
) {
    var notes by remember { mutableStateOf(currentNotes) }

    Column {

    }

}