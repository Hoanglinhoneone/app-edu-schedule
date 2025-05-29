package com.hiendao.eduschedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.ui.theme.outlineLight
import com.hiendao.eduschedule.ui.theme.primaryLight
import com.hiendao.eduschedule.ui.theme.surfaceContainerLowLight
import com.hiendao.eduschedule.utils.Constants
import com.hiendao.eduschedule.utils.entity.Day
import com.hiendao.eduschedule.utils.entity.DayOfMonth
import com.hiendao.eduschedule.utils.entity.DayOfWeek

@Composable
fun DayPickerModal(
    type: Int = Constants.TypeDay.WEEK,
    days: List<DayOfWeek> = listOf(
        DayOfWeek(Day.Sunday, isSelected = false),
        DayOfWeek(Day.Monday, isSelected = true),
        DayOfWeek(Day.Tuesday, isSelected = false),
        DayOfWeek(Day.Wednesday, isSelected = false),
        DayOfWeek(Day.Thursday, isSelected = true),
        DayOfWeek(Day.Friday, isSelected = false),
        DayOfWeek(Day.Saturday, isSelected = false),
    ),
    dayOfMonths: List<DayOfMonth> = emptyList(),
    onChangeDay: (DayOfWeek) -> Unit = {},
    onChangeDayOfMonth: (DayOfMonth) -> Unit = {},
) {
    when(type) {
        Constants.TypeDay.DAY -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
            ) {
                items(days) { item ->
                    DayItem(type = Constants.TypeDay.DAY ,isSelected = true, day = item.day, onClick = {
                        onChangeDay(item)
                    })
                }
            }
        }
        Constants.TypeDay.WEEK -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
            ) {
                items(days) { item ->
                    DayItem(type = Constants.TypeDay.WEEK ,isSelected = item.isSelected, day = item.day, onClick = {
                        onChangeDay(item)
                    })
                }
            }
        }
        Constants.TypeDay.MONTH -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
            ) {
                items(dayOfMonths) { item ->
                    DayItem(type = Constants.TypeDay.MONTH ,isSelected = item.isSelected, dayOfMonth = item.day, onClick = {
                        onChangeDayOfMonth(item)
                    })
                }
            }
        }

    }
}

@Composable
fun DayItem(
    type: Int = Constants.TypeDay.WEEK,
    isSelected: Boolean,
    onClick: () -> Unit,
    day: Day = Day.Sunday,
    dayOfMonth: Int = 1,
    modifier: Modifier = Modifier
) {
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.secondary
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if(type != Constants.TypeDay.MONTH) {
            Text(
                text = stringResource(day.shortTitle),
                fontSize = 12.sp,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = dayOfMonth.toString(),
                fontSize = 12.sp,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayPickerModalPreview(modifier: Modifier = Modifier) {
    EduScheduleTheme {
//        DayItem(
//            isSelected = true,
//            onClick = {},
//            day = Day.Sunday,
//            modifier = modifier
//        )
        DayPickerModal()
    }
}