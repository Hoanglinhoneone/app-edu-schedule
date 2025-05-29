package com.hiendao.eduschedule.control.datasource.remote.api.alarm

import com.hiendao.eduschedule.ui.screen.alarm.AlarmItem
import com.hiendao.eduschedule.utils.convertToTimeInMillis

data class AlarmDTO(
    val id: Int,
    val name: String,
    val entityID: Int?,
    val category: String,
    val timeAlarm: String,
    val repeatDays: List<String>,
    val mode: String,
    val music: String,
    val state: String
)

fun AlarmDTO.toAlarmItem() : AlarmItem {
    val alarmType = when(category){
        "GENERAL" -> 0
        "LESSON" -> 1
        "ASSIGNMENT" -> 2
        "PERSONAL_WORK" -> 3
        else -> 0
    }
    return AlarmItem(
        id = id,
        name = name,
        timeAlarm = timeAlarm.substring(11, timeAlarm.length),
        isActive = state == "active",
        isRepeat = repeatDays.isNotEmpty(),
        repeatType = repeatDays.joinToString(", "),
        repeatTime = repeatDays,
        isVibrate = mode == "VIBRATE",
        soundUri = music,
        alarmType = alarmType,
        date = timeAlarm.substring(0, 10),
        timeInMillis = timeAlarm.convertToTimeInMillis(),
        soundName = music
    )
}

fun AlarmItem.toAlarmDTO(): AlarmDTO {
    val category = when(alarmType){
        0 -> "GENERAL"
        1 -> "LESSON"
        2 -> "ASSIGNMENT"
        3 -> "PERSONAL_WORK"
        else -> "GENERAL"
    }
    return AlarmDTO(
        id = id,
        name = name,
        entityID = entityId,
        category = category,
        timeAlarm = "${date}T${timeAlarm}:00",
        repeatDays = repeatTime ?: emptyList(),
        mode = if (isVibrate) "VIBRATE" else "SOUND",
        music = soundUri,
        state = if (isActive) "ACTIVE" else "INACTIVE"
    )
}

data class AlarmBody(
    val name: String,
    val entityID: Int?,
    val category: String,
    val timeAlarm: String,
    val repeatDays: List<String>,
    val mode: String,
    val music: String,
    val state: String
)

fun AlarmDTO.toAlarmBody(): AlarmBody{
    return AlarmBody(
        name, entityID, category, timeAlarm, repeatDays, mode, music, state
    )
}