package com.hiendao.eduschedule.utils

import com.hiendao.eduschedule.utils.entity.Day
import com.hiendao.eduschedule.utils.entity.DayOfMonth
import com.hiendao.eduschedule.utils.entity.DayOfWeek

fun String.changeStateAssignment(endTime: String): String {
    val currentTime = System.currentTimeMillis()
    return when (this) {
        "COMPLETE" -> {
            if (currentTime >= endTime.convertToTimestamp()) {
                "OVERDUE"
            } else {
                "INCOMPLETE"
            }
        }

        "INCOMPLETE" -> "COMPLETE"
        else -> "COMPLETE"
    }
}

fun checkValidTime(startTime: String): Boolean {
    return startTime.convertToTimeInMillis() <= System.currentTimeMillis()
}

fun String.normalizeString(): String {
    val normalized = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return normalized.replace("\\p{M}".toRegex(), "")
}


fun convertStringToListDayOfWeek(days: String): List<DayOfWeek> {
    val listDayOfWeekDefault = mutableListOf(
        DayOfWeek(Day.Sunday, isSelected = false),
        DayOfWeek(Day.Monday, isSelected = false),
        DayOfWeek(Day.Tuesday, isSelected = false),
        DayOfWeek(Day.Wednesday, isSelected = false),
        DayOfWeek(Day.Thursday, isSelected = false),
        DayOfWeek(Day.Friday, isSelected = false),
        DayOfWeek(Day.Saturday, isSelected = false),
    )
    days.split(",").forEach { day ->
        listDayOfWeekDefault.forEachIndexed { index, dayOfWeek ->
            if (dayOfWeek.day.name == day)
                listDayOfWeekDefault[index] = dayOfWeek.copy(isSelected = true)
        }
    }
    return listDayOfWeekDefault
}

fun convertStringToListDayOfMonth(days: String): List<DayOfMonth> {
    val listDayOfMonth = mutableListOf<DayOfMonth>()
    days.split(",").forEach { day ->
        listDayOfMonth.add(DayOfMonth(day.split("/")[0].toInt(), true))
    }
    return listDayOfMonth
}


