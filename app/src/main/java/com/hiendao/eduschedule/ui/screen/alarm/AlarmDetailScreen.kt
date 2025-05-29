package com.hiendao.eduschedule.ui.screen.alarm

import android.content.Intent
import android.media.RingtoneManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hiendao.eduschedule.R
import com.hiendao.eduschedule.ui.component.BottomChangeName
import com.hiendao.eduschedule.ui.component.ConfirmDeleteDialog
import com.hiendao.eduschedule.ui.component.ContentChangeName
import com.hiendao.eduschedule.ui.component.DatePickerModal
import com.hiendao.eduschedule.ui.screen.home_schedule.Event
import com.hiendao.eduschedule.ui.theme.EduScheduleTheme
import com.hiendao.eduschedule.utils.base.Picker
import com.hiendao.eduschedule.utils.base.rememberPickerState
import com.hiendao.eduschedule.utils.convertDateToMillis
import com.hiendao.eduschedule.utils.convertMillisToDate
import com.hiendao.eduschedule.utils.convertToTimeInMillis
import com.hiendao.eduschedule.utils.formatTime
import com.hiendao.eduschedule.utils.toDateAndTimeString

@Preview(showBackground = true)
@Composable
fun PreviewAlarmDetailScreen(modifier: Modifier = Modifier) {
    EduScheduleTheme {
        AlarmDetailScreen(
            modifier = modifier,
            alarmUiState = AlarmUiState()
        )
    }
}

@Composable
fun AlarmDetailScreen(
    modifier: Modifier = Modifier,
    alarmUiState: AlarmUiState = AlarmUiState(),
    onClickClose: () -> Unit = {},
    onClickDone: (AlarmItem) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    isAddAlarm: Boolean = false,
    event: Event ?= null
) {
    var alarmItem = alarmUiState.selectedAlarm ?: mockAlarmItems[0]

    if(isAddAlarm){
        val currentDateAndTime = System.currentTimeMillis().toDateAndTimeString()
        val eventTime = event?.timeStart?.convertToTimeInMillis()?.toDateAndTimeString() ?: event?.timeEnd?.convertToTimeInMillis()?.toDateAndTimeString()
        alarmItem = AlarmItem(
            id = 0,
            name = event?.name ?: "",
            timeAlarm = eventTime?.second ?: currentDateAndTime.second,
            date = eventTime?.first ?:  currentDateAndTime.first,
            isRepeat = false,
            repeatTime = mutableListOf(),
            repeatType = "",
            isVibrate = false,
            soundName = "",
            soundUri = "",
            isActive = true
        )
    }

    val context = LocalContext.current

    val alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    val ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone)
    val defaultTitle = ringtoneAlarm.getTitle(context)

    val hour = alarmItem.timeAlarm.split(":")[0]
    val minute = alarmItem.timeAlarm.split(":")[1]
    //Time
    val values = remember { (0..23).map { it.toString() } }
    val valuesPickerState = rememberPickerState()
    val minuteValues = remember { (0..59).map { it.toString() } }
    val minuteValuesPickerState = rememberPickerState()

    //Repeat
    var isRepeat by remember { mutableStateOf(alarmItem.isRepeat) }

    //Sound Name & Uri
    var soundName by remember { mutableStateOf(if(alarmItem.soundName.isEmpty()) defaultTitle else alarmItem.soundName) }
    var soundUri by remember { mutableStateOf(if(alarmItem.soundUri.isEmpty()) alarmTone.toString() else alarmItem.soundUri)}

    //Alarm Name
    var alarmName by remember { mutableStateOf(alarmItem.name) }
    var isShowNameBottomSheet by remember { mutableStateOf(false) }

    //Date
    var scheduledDate by remember { mutableStateOf(alarmItem.date) }
    var isShowDatePicker by remember { mutableStateOf(false) }

    //Show dialog confirm
    var isShowDialog by remember { mutableStateOf(false) }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val ringtoneAlarm = RingtoneManager.getRingtone(context, it)
                soundName = ringtoneAlarm.getTitle(context)
                soundUri = it.toString()
            }
        }
    )

    Box(modifier = modifier){
        if(alarmUiState.isLoading) {
            // Show loading indicator
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }
        Column{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onClickClose()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }

                Text(
                    text = if(!isAddAlarm) stringResource(R.string.update_alarm) else stringResource(R.string.add_alarm),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )

                Row {
                    IconButton(onClick = {
                        onClickDone(
                            alarmItem.copy(
                                name = alarmName,
                                timeAlarm = formatTime(valuesPickerState.selectedItem.toInt(), minuteValuesPickerState.selectedItem.toInt()),
                                date = scheduledDate,
                                isRepeat = isRepeat,
                                soundName = soundName,
                                soundUri = soundUri
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done"
                        )
                    }

                    if(!isAddAlarm){
                        Spacer(Modifier.width(12.dp))

                        IconButton(onClick = {
//                        onClickDelete(alarmItem.id)
                            isShowDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Picker(
                        state = valuesPickerState,
                        items = values,
                        visibleItemsCount = 5,
                        modifier = Modifier.width(100.dp),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 32.sp),
                        startIndex = values.indexOf(hour.toInt().toString())
                    )
                    Text(
                        text = stringResource(R.string.hour),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Picker(
                        state = minuteValuesPickerState,
                        items = minuteValues,
                        visibleItemsCount = 5,
                        modifier = Modifier.width(100.dp),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 32.sp),
                        startIndex = minuteValues.indexOf(minute.toInt().toString())
                    )
                    Text(
                        text = stringResource(R.string.minutes),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isShowNameBottomSheet = true
                    }
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.alarm_name),
                    fontSize = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = alarmName,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Select sound",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Open bottom sheet for name
                        isShowDatePicker = true
                    }
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.alarm_date),
                    fontSize = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = scheduledDate,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Schedule date",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Handle sound selection -> Intent to open sound picker
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    }
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.alarm_sound),
                    fontSize = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = soundName,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Select sound",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.vibration_when_alarm),
                    fontSize = 16.sp
                )

                Switch(
                    checked = isRepeat,
                    onCheckedChange = { isChecked ->
                        // Handle vibration toggle
                        isRepeat = isChecked
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if(isShowNameBottomSheet){
                if(event != null){
                    Dialog(
                        onDismissRequest = {isShowNameBottomSheet = false}
                    ) {
                        ContentChangeName(
                            modifier = modifier.fillMaxWidth().background(Color.White).clip(
                                RoundedCornerShape(16.dp)
                            ),
                            onClickChangeName = { newName ->
                                alarmName = newName
                                isShowNameBottomSheet = false
                            },
                            name = alarmName,
                            onDismiss = {
                                isShowNameBottomSheet = false
                            }
                        )
                    }
                } else {
                    BottomChangeName(
                        modifier = modifier.fillMaxWidth(),
                        onClickChangeName = { newName ->
                            alarmName = newName
                            isShowNameBottomSheet = false
                        },
                        name = alarmName,
                        onDismiss = {
                            isShowNameBottomSheet = false
                        }
                    )
                }
            }

            if(isShowDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        isShowDatePicker = false
                        scheduledDate = it.convertMillisToDate()
                    },
                    onDismiss = { isShowDatePicker = false },
                    initialDate = scheduledDate.convertDateToMillis("dd/MM/yyyy") + 86400000L
                )
            }

            if(isShowDialog){
                ConfirmDeleteDialog(
                    onDismiss = {
                        isShowDialog = false
                    },
                    onConfirm = {
                        isShowDialog = false
                        onClickDelete(alarmItem.id)
                    },
                    title = stringResource(R.string.title_delete_alarm),
                    descriptionText = stringResource(R.string.description_delete_alarm),
                    confirmButton = stringResource(R.string.delete),
                    dismissButton = stringResource(R.string.cancel)
                )
            }
        }
    }
}