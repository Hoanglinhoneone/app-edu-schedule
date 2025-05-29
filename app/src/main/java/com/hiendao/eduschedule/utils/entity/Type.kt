package com.hiendao.eduschedule.utils.entity

import androidx.annotation.StringRes
import com.hiendao.eduschedule.R

enum class TypeAdd(val title: Int) {
    Event(R.string.event),
    Course(R.string.course),
    Assignment(R.string.assignment),
    Note(R.string.note)
}

enum class StateAssignment(val title: Int, val converter: String) {
    Complete(R.string.complete, "COMPLETE" ),
    Incomplete(R.string.incomplete, "INCOMPLETE"),
    Overdue(R.string.overdue, "OVERDUE")
}

enum class Repeat(val title: Int, val converter : String) {
    Daily(R.string.daily, "DAILY"),
    Weekly(R.string.weekly, "WEEKLY"),
    Monthly(R.string.monthly, "MONTHLY"),
    Yearly(R.string.yearly, "YEARLY"),
    None(R.string.no_repeat, "NONE")
}

enum class StateCourse(val title: Int, val converter : String) {
    Ongoing(R.string.ongoing, "ONGOING"),
    NotYet(R.string.course_not_yet, "NOT_YET"),
    Ended(R.string.ended, "END"),
}

enum class StateLesson(val title: Int, val converter : String) {
    Present(R.string.present,"PRESENT"),
    Absent(R.string.absent,"ABSENT"),
    NotYet(R.string.not_yet, "NOT_YET")
}

enum class FilterCourse(val title: Int, val time: Int) {
    All(R.string.all, 0),
    One(R.string.one, 2023),
    Two(R.string.two, 2024),
    Three(R.string.three, 2025),
    Now(R.string.four, 2025),
}

enum class TypeContact(val icon: Int) {
    Core(R.drawable.ic_support),
    Zalo(R.drawable.ic_zalo),
    Email(R.drawable.ic_gmail)
}

enum class Day(@StringRes val title: Int, @StringRes val shortTitle: Int) {
    Monday(R.string.monday, R.string.summary_monday),
    Tuesday(R.string.tuesday, R.string.summary_tuesday),
    Wednesday(R.string.wednesday, R.string.summary_wednesday),
    Thursday(R.string.thursday, R.string.summary_thursday),
    Friday(R.string.friday, R.string.summary_friday),
    Saturday(R.string.saturday, R.string.summary_saturday),
    Sunday(R.string.sunday, R.string.summary_sunday)
}