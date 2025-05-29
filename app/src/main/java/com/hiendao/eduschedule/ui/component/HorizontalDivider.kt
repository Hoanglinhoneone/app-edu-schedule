package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalItemDivider(
    thickness: Dp = DividerDefaults.Thickness,
    modifier: Modifier = Modifier,
    color: Color = DividerDefaults.color
) {
    HorizontalDivider(
        thickness = thickness,
        modifier = modifier,
        color = color
    )
}