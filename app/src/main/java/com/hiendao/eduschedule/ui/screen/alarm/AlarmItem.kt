package com.hiendao.eduschedule.ui.screen.alarm

data class AlarmItem(
    val id: Int,
    val name: String,
    val timeAlarm: String,
    var isActive: Boolean,
    val isRepeat: Boolean,
    val repeatType: String,
    val repeatTime: List<String> ?= null,
    val isVibrate: Boolean,
    val soundUri: String,
    val alarmType: Int = 0,
    val date: String = "12/05/2025",
    val timeInMillis: Long = System.currentTimeMillis() + 60 * 5000,
    val soundName: String = "Default",
    val entityId: Int? = null
)
val mockAlarmItems = listOf(
    AlarmItem(
        id = 1,
        name = "Wake Up",
        timeAlarm = "07:00",
        isActive = true,
        isRepeat = true,
        repeatType = "Daily",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_1.mp3",
        alarmType = 2
    ),
    AlarmItem(
        id = 2,
        name = "Workout",
        timeAlarm = "06:00",
        isActive = true,
        isRepeat = true,
        repeatType = "Weekdays",
        repeatTime = null,
        isVibrate = false,
        soundUri = "alarm_sound_2.mp3",
        alarmType = 2
    ),
    AlarmItem(
        id = 3,
        name = "Meeting Reminder",
        timeAlarm = "09:00",
        isActive = false,
        isRepeat = false,
        repeatType = "",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_3.mp3",
        alarmType = 1
    ),
    AlarmItem(
        id = 4,
        name = "Lunch Break",
        timeAlarm = "12:30",
        isActive = true,
        isRepeat = true,
        repeatType = "Daily",
        repeatTime = null,
        isVibrate = false,
        soundUri = "alarm_sound_4.mp3",
        alarmType = 1
    ),
    AlarmItem(
        id = 5,
        name = "Evening Walk",
        timeAlarm = "06:00",
        isActive = true,
        isRepeat = true,
        repeatType = "Weekends",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_5.mp3",
        alarmType = 2
    ),
    AlarmItem(
        id = 6,
        name = "Study Time",
        timeAlarm = "08:00",
        isActive = false,
        isRepeat = true,
        repeatType = "Daily",
        repeatTime = null,
        isVibrate = false,
        soundUri = "alarm_sound_6.mp3",
        alarmType = 3
    ),
    AlarmItem(
        id = 7,
        name = "Medication Reminder",
        timeAlarm = "09:00",
        isActive = true,
        isRepeat = true,
        repeatType = "Daily",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_7.mp3",
        alarmType = 1
    ),
    AlarmItem(
        id = 8,
        name = "Project Deadline",
        timeAlarm = "11:59",
        isActive = true,
        isRepeat = false,
        repeatType = "",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_8.mp3",
        alarmType = 3
    ),
    AlarmItem(
        id = 9,
        name = "Morning Yoga",
        timeAlarm = "05:30",
        isActive = true,
        isRepeat = true,
        repeatType = "Weekdays",
        repeatTime = null,
        isVibrate = false,
        soundUri = "alarm_sound_9.mp3",
        alarmType = 2
    ),
    AlarmItem(
        id = 10,
        name = "Call Mom",
        timeAlarm = "08:00",
        isActive = false,
        isRepeat = false,
        repeatType = "",
        repeatTime = null,
        isVibrate = true,
        soundUri = "alarm_sound_10.mp3",
        alarmType = 1
    )
)