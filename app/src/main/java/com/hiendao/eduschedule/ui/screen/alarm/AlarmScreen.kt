package com.hiendao.eduschedule.ui.screen.alarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiendao.eduschedule.R

@Composable
fun AlarmRoute(
    modifier: Modifier = Modifier,
    alarmUiState: AlarmUiState,
    onClickAlarm: (Int) -> Unit,
    onCheckedChange: (Int, Boolean) -> Unit,
    onClickAddAlarm: () -> Unit,
    onSelectAlarm: (Int) -> Unit = {},
    onFilterAlarm: (Int) -> Unit = {}
) {
    Box(modifier = modifier) {
        if (alarmUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
        AlarmScreen(
            modifier = modifier,
            alarmUiState = alarmUiState,
            onClickAlarm = onClickAlarm,
            onClickAddAlarm = onClickAddAlarm,
            onCheckedChange = onCheckedChange,
            selectAlarm = onSelectAlarm,
            onFilterAlarm = onFilterAlarm
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmScreen(
    modifier: Modifier = Modifier,
    alarmUiState: AlarmUiState = AlarmUiState(),
    onClickAlarm: (Int) -> Unit = {},
    onFilterAlarm: (Int) -> Unit = {},
    onClickAddAlarm: () -> Unit = {},
    selectAlarm: (Int) -> Unit = {},
    onCheckedChange: (Int, Boolean) -> Unit = { _, _ -> }
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onClickAddAlarm.invoke()
            }, shape = CircleShape) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 30.dp)
        ) {
            ScheduleFilters(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                onFilterSelected = { filter ->
                    // Handle filter selection
                    onFilterAlarm(filter)
                }
            )

            if(alarmUiState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(50.dp)
                )
            }

            if(alarmUiState.filterAlarmList.isEmpty() && !alarmUiState.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_empty_course),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Empty Event"
                    )
                    Text(
                        text = stringResource(R.string.no_alarm),
                        modifier = Modifier.padding(top = 50.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 50.dp)
                ) {
                    items(alarmUiState.filterAlarmList.size) { index ->
                        val alarmItem = alarmUiState.filterAlarmList[index]
                        AlarmItemScreen(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            alarmItem = alarmItem,
                            onCheckedChange = { isActive ->
                                // Handle switch state change
                                alarmItem.isActive = isActive
                                onCheckedChange(alarmItem.id, isActive)
//                        alarmViewModel.updateAlarmState(alarmItem.id, isActive)
                            },
                            onAlarmItemClick = { id ->
                                // Handle alarm item click
                                selectAlarm(id)
                                onClickAlarm(id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmItemScreen(
    modifier: Modifier = Modifier,
    alarmItem: AlarmItem,
    onCheckedChange: (Boolean) -> Unit = {},
    onAlarmItemClick: (Int) -> Unit = {}
) {
    var isActive by remember {
        mutableStateOf(alarmItem.isActive)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onAlarmItemClick.invoke(alarmItem.id)
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.alarm_clock),
                contentDescription = null,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = CircleShape
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Column {
                Text(
                    text = alarmItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Text(
                    text = alarmItem.timeAlarm,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = alarmItem.repeatType,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Switch(
            checked = isActive,
            onCheckedChange = {
                isActive = it
                onCheckedChange.invoke(it)
            },
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}


@Composable
fun ScheduleFilters(
    modifier: Modifier = Modifier,
    onFilterSelected: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val selectedChip = remember { mutableIntStateOf(0) }
    val listChipItems =
        listOf(
            context.getString(R.string.txt_all),
            context.getString(R.string.event),
            context.getString(R.string.course),
            context.getString(R.string.assignment),
            context.getString(R.string.note)
        )
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(listChipItems.size) { index ->
            FilterChip(
                selected = selectedChip.intValue == index,
                onClick = {
                    selectedChip.intValue = index
                    onFilterSelected(index)
                },
                label = { Text(listChipItems[index]) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
