package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgress(
    progressSize: Dp = 100.dp,
    strokeWidth: Dp = 4.dp,
    padding: Dp = 16.dp,
    color: Color = Color.White,
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier
            .size(progressSize)
            .padding(padding),
        color = color,
        strokeWidth = strokeWidth
    )
}