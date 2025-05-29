package com.hiendao.eduschedule.ui.screen.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme

@Composable
fun UtilitiesScreen(
    onClickCourse : () -> Unit,
    onClickAlarm : () -> Unit,
    onClickAssignment : () -> Unit,
    onClickStatistic : () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UtilItem(
            iconStart = R.drawable.ic_course,
            title = stringResource(R.string.course),
            onClick = { onClickCourse () }
        )
        UtilItem(
            iconStart = R.drawable.ic_clock,
            title = stringResource(R.string.alarm),
            onClick = { onClickAlarm () }
        )
        UtilItem(
            iconStart = R.drawable.ic_line_chart,
            title = stringResource(R.string.statistical),
            onClick = { onClickStatistic () }
        )
    }
}

@Composable
fun UtilItem(
    iconStart: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {

    val background = MaterialTheme.colorScheme.surfaceDim
    val tintIcon = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(start = 8.dp, end = 8.dp)
            .background(background, shape = RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(iconStart),
                contentDescription = null,
                tint = tintIcon
            )
            Text(text = title,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 24.dp)
                )
        }
        Icon(
            painter = painterResource(R.drawable.ic_right_arrow),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 32.dp),
            tint = tintIcon
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UtilPreview(modifier: Modifier = Modifier) {
    EduScheduleTheme {
        UtilitiesScreen(
            onClickCourse = {},
            onClickAlarm = {},
            onClickAssignment = {},
            onClickStatistic = {},
        )
    }
}