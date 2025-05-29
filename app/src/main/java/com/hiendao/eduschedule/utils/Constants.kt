package com.hiendao.eduschedule.utils

import com.hiendao.eduschedule.utils.entity.TypeAdd

object Constants {
    const val PREFERENCE_NAME = "sharedPreferences"
//    const val LOCALHOST = "172.20.10.2"
//    const val LOCALHOST = "192.168.1.184"
    const val LOCALHOST = "172.11.47.133"
//    const val LOCALHOST = "192.168.0.102"
//    const val LOCALHOST = "192.168.0.114"


    const val ALARM_CHANNEL_NAME = "Channel_ID"
    const val NOTIFICATION_ID = 1
    const val VERIFY_USER = "VerifyUser"
    const val CHANGE_PASSWORD = "ChangePassword"
    const val FORGOT_PASSWORD = "ForgotPassword"
    const val TIME_EXPIRED_OTP_CODE = 3600_000L // 1 hour

    const val TYPE_ALL = 0
    const val TYPE_EVENT = 1
    const val TYPE_COURSE = 2
    const val TYPE_ASSIGNMENT = 3
    const val TYPE_NOTE = 4

    object HttpErrorMessage {
        const val OK = "OK"
        const val NO_INTERNET = "No internet connection"
        const val UNKNOWN = "Unknown error"
        const val UNAUTHORIZED = "Unauthorized"
        const val MISSING_INFO = "Missing Info"
        const val INCORRECT_INFO = "Incorrect Info"
        const val NEED_REFRESH_TOKEN = "Need refresh token"
        const val EMPTY = "Empty"
    }
    object TypeDialog {
        const val REPEAT = 0
        const val STATE_LESSON = 1
        const val STATE_COURSE = 2
    }

    object TypeDay {
        const val DAY = 0
        const val WEEK = 1
        const val MONTH = 2
    }

    object AssignmentPersonalState {
        const val COMPLETE = "COMPLETE"
        const val INCOMPLETED = "INCOMPLETED"
        const val OVERDUE = "OVERDUE"
    }

    object ScheduleLearningState {
        const val PRESENT = "PRESENT"
        const val ABSENT = "ABSENT"
        const val NOT_YET = "NOT_YET"
    }

    object NotificationState {
        const val READ = "READ"
        const val UNREAD = "UNREAD"
    }

    object EventType {
        const val NOTIFICATION = "NOTIFICATION"
        const val COURSE = "COURSE"
        const val ASSIGNMENT = "ASSIGNMENT"
        const val LESSON = "LESSON"
        const val PERSONAL_WORK = "PERSONAL_WORK"
        const val ALARM = "ALARM"
    }

    const val ADD_COURSE = 0
    const val UPDATE_COURSE = 1

    val listTypeAdd: List<TypeAdd> = listOf(
        TypeAdd.Event, TypeAdd.Course, TypeAdd.Assignment, TypeAdd.Note
    )
}

