package com.hiendao.eduschedule.ui.screen.home_schedule

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.control.datasource.remote.api.alarm.toAlarmItem
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.screen.add.AddScreen
import com.hiendao.eduschedule.ui.screen.alarm.AlarmDetailScreen
import com.hiendao.eduschedule.ui.screen.alarm.AlarmItem
import com.hiendao.eduschedule.ui.screen.alarm.AlarmUiState
import com.hiendao.eduschedule.utils.DataSource
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.entity.TypeAdd
import com.hiendao.eduschedule.utils.getDayOfWeek
import com.hiendao.eduschedule.utils.getStartOfDayTimestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    uiState: ScheduleUiState,
    changeEventState: UpdateStateEventResource ?= null,
    getAllSchedules: () -> Unit,
    updateEvent: (Event, Boolean) -> Unit,
    updateTimes: (Long) -> Unit,
    filterByTime: (Long) -> Unit,
    filterByCategory: (Int) -> Unit,
    onClickDoneAlarm: (Boolean, AlarmItem) -> Unit = {_, _ -> },
    onClickDeleteAlarm: (Int) -> Unit = {},
    navigateToLogin: () -> Unit = {}
) {
    var isShowDatePicker by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(getStartOfDayTimestamp()) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var filterSelected by remember { mutableIntStateOf(0) }

    var selectedItem by remember { mutableStateOf<Event?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var eventType by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(changeEventState) {
        val updateSuccessText = when(eventType){
            ScheduleType.ASSIGNMENT.toString() -> context.getString(R.string.assignment)
            ScheduleType.PERSONAL_WORK.toString() -> context.getString(R.string.personal_work)
            ScheduleType.LESSON.toString() -> context.getString(R.string.schedule_learning)
            else -> context.getString(R.string.event)
        }
        if(changeEventState?.isSuccess == true){
            Toast.makeText(
                context,
                context.getString(R.string.update_event_success, updateSuccessText),
                Toast.LENGTH_SHORT
            ).show()
            getAllSchedules()
        } else if(!changeEventState?.errorMsg.isNullOrEmpty()){
            Toast.makeText(
                context,
                context.getString(R.string.update_event_failed, updateSuccessText),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(uiState) {
        if(uiState.errorMsg != null){
            navigateToLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            uiState.currentMonthAndYear,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isShowDatePicker = true }) {
                        Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0x99F3C4FB))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isBottomSheetVisible = true
                },
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if(changeEventState?.isLoading == true){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                Modifier.fillMaxSize()
            ) {
                println("list time: ${uiState.currentTimeList} with start time: $startTime and index: ${getDayOfWeek(startTime)}")
                // Week Calendar View
                WeekCalendarView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0x99F3C4FB))
                        .padding(bottom = 8.dp),
                    listDate = uiState.currentTimeList,
                    onDaySelected = {
                        filterByTime(it)
                    },
                    selectedIndex = getDayOfWeek(uiState.selectedTime),
                )
                // Schedule Filters
                ScheduleFilters(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    onFilterSelected = { filter ->
                        // Handle filter selection
                        filterByCategory(filter)
                        filterSelected = filter
                    }
                )

                // Schedule List
                ScheduleList(
                    uiState, filterSelected,
                    onAlarmClick = { event ->
                        selectedItem = event
                        isDialogVisible = true
                    },
                    onJoinedClick = {
                        eventType = it.type.toString()
                        updateEvent(it, true)
                    },
                    onMissedClick = {
                        eventType = it.type.toString()
                        updateEvent(it, false)
                    }
                )
            }
            if(isShowDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        startTime = it
                        updateTimes(it)
                        get7DaysFromDate(it).let { listDate ->
                            uiState.currentTimeList.clear()
                            uiState.currentTimeList.addAll(listDate)
                        }
                    },
                    onDismiss = { isShowDatePicker = false },
                    initialDate = uiState.selectedTime + 86400000L
                )
            }

            if (isBottomSheetVisible) {
                AddScreen (
                    typeSelected = TypeAdd.Event,
                    onCloseSheet = { isBottomSheetVisible = false },
                    onAddSuccess = {
                        getAllSchedules()
                    }
                )
            }

            if (isDialogVisible) {
                val alarm =
                    DataSource.user.listAlarm?.firstOrNull { it.entityID == selectedItem?.id }
                Dialog(
                    onDismissRequest = { isDialogVisible = false },
                ) {
                    AlarmDetailScreen(
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(16.dp)
                        ),
                        alarmUiState = AlarmUiState(
                            selectedAlarm = alarm?.toAlarmItem()
                        ),
                        onClickClose = { isDialogVisible = false },
                        onClickDone = { alarmItem ->
                            // Handle save alarm logic here
                            onClickDoneAlarm(alarm == null, alarmItem.copy(
                                entityId = selectedItem?.id,
                                alarmType = when(selectedItem?.type){
                                    ScheduleType.ASSIGNMENT -> 2
                                    ScheduleType.PERSONAL_WORK -> 3
                                    ScheduleType.LESSON -> 1
                                    null -> 0
                                }
                            ))
                            isDialogVisible = false
                        },
                        onClickDelete = { alarmId ->
                            // Handle delete alarm logic here
                            onClickDeleteAlarm(alarmId)
                            isDialogVisible = false
                        },
                        isAddAlarm = alarm == null,
                        event = selectedItem
                    )
                }
            }
        }
    }
}

private fun get7DaysFromDate(date: Long): List<DateItem> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = date
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }
    val listDate = mutableListOf<DateItem>()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dayFormat = SimpleDateFormat("E", Locale.getDefault())
    for (i in 0 until 7) {
        println("First: ${calendar.timeInMillis}")
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDate = calendar.timeInMillis
        println("Current: $currentDate")
        listDate.add(DateItem(currentDate, dateFormat.format(Date(currentDate)), dayFormat.format(Date(currentDate))))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    println("List Date: $listDate")
    return listDate
}

@Composable
fun WeekCalendarView(
    modifier: Modifier = Modifier,
    onDaySelected: (Long) -> Unit = {},
    listDate: List<DateItem> = listOf(),
    selectedIndex: Int
) {
    var selectedDay by remember(selectedIndex) {
        mutableIntStateOf(selectedIndex)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listDate.forEachIndexed { index, day ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        selectedDay = index
                        onDaySelected(listDate[index].dateInMillis)
                    }
                    .background(if (index == selectedDay) Color(0xFFBA68C8) else Color(0x99DEB5E4))
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day.dayOfWeek,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        fontWeight = if (index == selectedDay) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = day.date.substring(0, 2),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = if (index == selectedDay) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
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
            context.getString(R.string.schedule_learning),
            context.getString(R.string.assignment),
            context.getString(R.string.personal_work)
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

@Composable
fun ScheduleList(
    uiState: ScheduleUiState,
    filter: Int,
    modifier: Modifier = Modifier,
    onAlarmClick: (Event) -> Unit = {},
    onJoinedClick: (Event) -> Unit = {},
    onMissedClick: (Event) -> Unit = {}
) {
    val context = LocalContext.current
    val filterSelected by remember(filter) { mutableIntStateOf(filter) }

    val eventType = when (filterSelected) {
        0 -> context.getString(R.string.event)
        1 -> context.getString(R.string.schedule_learning)
        2 -> context.getString(R.string.assignment)
        3 -> context.getString(R.string.personal_work)
        else -> context.getString(R.string.event)
    }

    Box(modifier = modifier){
        if(uiState.isLoading){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        if(uiState.currentEvents.isEmpty() && !uiState.isLoading) {
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
                    text = stringResource(R.string.empty_type, eventType),
                    modifier = Modifier.padding(top = 50.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 64.dp)
            ) {
                items(uiState.currentEvents.size) { index ->
                    ScheduleItemCard(
                        uiState.currentEvents[index],
                        onAlarmClick = { event ->
                            onAlarmClick(event)
                        },
                        onJoinedClick = {
                            onJoinedClick(it)
                        },
                        onMissedClick = {
                            onMissedClick(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleItemCard(
    item: Event,
    onAlarmClick: (Event) -> Unit = {},
    onJoinedClick: (Event) -> Unit = {},
    onMissedClick: (Event) -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val context = LocalContext.current
    val confirmText = when(item.type){
        ScheduleType.ASSIGNMENT -> context.getString(R.string.complete)
        ScheduleType.PERSONAL_WORK -> context.getString(R.string.complete)
        ScheduleType.LESSON -> context.getString(R.string.joined)
    }
    val cancelText = when(item.type){
        ScheduleType.ASSIGNMENT -> context.getString(R.string.incomplete)
        ScheduleType.PERSONAL_WORK -> context.getString(R.string.incomplete)
        ScheduleType.LESSON -> context.getString(R.string.missed)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    if(!item.timeStart.isNullOrEmpty()){
                        Text(
                            text = item.timeStart.substring(11, 16),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                    if (!item.timeEnd.isNullOrEmpty()) {
                        Text(
                            text = item.timeEnd.substring(11, 16),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0x4D000000)
                        )
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    thickness = 2.dp,
                )

                Column(
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Text(
                            item.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            maxLines = 2
                        )

                        if(DataSource.user.listCourse?.find { it.id == (item.courseId ?: -1) } != null){
                            Text(
                                text = " - ${DataSource.user.listCourse?.find { it.id == (item.courseId ?: -1) }?.name}",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Text(text = dateFormat.format(item.date), color = Color.Gray)
                }
                Row {
                    IconButton(onClick = {
                        onAlarmClick(item)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Alarm,
                            contentDescription = "Turn On Alarm",
                            tint = if(DataSource.user.listAlarm?.any { it.entityID == item.id } == true) MaterialTheme.colorScheme.secondary else Color.Unspecified
                        )
                    }
                }
            }

            if(checkShowTick(item)){
                Row(
                    modifier = Modifier
                        .align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = {
                            onJoinedClick(item)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBA68C8),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Check")
                            Text(text = confirmText, fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            onMissedClick(item)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Check")
                        Text(text = cancelText, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

fun checkShowTick(event: Event): Boolean{
    val startOfDay = getStartOfDayTimestamp()
    val endOfDay = startOfDay + 86_399_999 // 1 ngày trừ 1ms

    return (event.date in startOfDay..endOfDay || event.endDate in startOfDay..endOfDay) && (event.state in listOf<String>("NOT_YET", "OVERDUE", "INCOMPLETE", "ABSENT"))
}