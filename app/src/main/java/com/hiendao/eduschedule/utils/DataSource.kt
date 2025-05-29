package com.hiendao.eduschedule.utils

import com.hiendao.eduschedule.utils.entity.Assignment
import com.hiendao.eduschedule.utils.entity.Course
import com.hiendao.eduschedule.utils.entity.Day
import com.hiendao.eduschedule.utils.entity.DayOfMonth
import com.hiendao.eduschedule.utils.entity.DayOfWeek
import com.hiendao.eduschedule.utils.entity.Lesson
import com.hiendao.eduschedule.utils.entity.StateAssignment
import com.hiendao.eduschedule.utils.entity.StateLesson
import com.hiendao.eduschedule.utils.entity.User

object DataSource {

    var user: User = User (
         id = 1,
         email = "quangteo3k@gmail.com",
         fullName = "Hoàng Ngọc Linh",
         mobilePhone = "0877662003",
         gender = "MALE",
         dateOfBirth = "30/05/2003",
    )
    val days: List<DayOfWeek> = listOf(
        DayOfWeek(Day.Sunday, isSelected = true),
        DayOfWeek(Day.Monday, isSelected = false),
        DayOfWeek(Day.Tuesday, isSelected = false),
        DayOfWeek(Day.Wednesday, isSelected = false),
        DayOfWeek(Day.Thursday, isSelected = false),
        DayOfWeek(Day.Friday, isSelected = false),
        DayOfWeek(Day.Saturday, isSelected = false),
    )

    val dayOfMonth: List<DayOfMonth> = listOf(
        DayOfMonth(1, isSelected = true),
        DayOfMonth(2, isSelected = false),
        DayOfMonth(3, isSelected = false),
        DayOfMonth(4, isSelected = false),
        DayOfMonth(5, isSelected = false),
        DayOfMonth(6, isSelected = false),
        DayOfMonth(7, isSelected = false),
        DayOfMonth(8, isSelected = false),
        DayOfMonth(9, isSelected = false),
        DayOfMonth(10, isSelected = false),
        DayOfMonth(11, isSelected = false),
        DayOfMonth(12, isSelected = false),
        DayOfMonth(13, isSelected = false),
        DayOfMonth(14, isSelected = false),
        DayOfMonth(15, isSelected = false),
        DayOfMonth(16, isSelected = false),
        DayOfMonth(17, isSelected = false),
        DayOfMonth(18, isSelected = false),
        DayOfMonth(19, isSelected = false),
        DayOfMonth(20, isSelected = false),
        DayOfMonth(21, isSelected = false),
        DayOfMonth(22, isSelected = false),
        DayOfMonth(23, isSelected = false),
        DayOfMonth(24, isSelected = false),
        DayOfMonth(25, isSelected = false),
        DayOfMonth(26, isSelected = false),
        DayOfMonth(27, isSelected = false),
        DayOfMonth(28, isSelected = false),
        DayOfMonth(29, isSelected = false),
        DayOfMonth(30, isSelected = false),
        DayOfMonth(31, isSelected = false),
    )
    val course: List<Course> = listOf(
        Course(1, credits = "0 tín chỉ",name =  "--Trống--", teacher = "Trống", location = "Trống", note = ""),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Hệ thống chuyên sâu nâng cao vip pro"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình phần mềm và kiểm thử"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Hệ thống chuyên sâu nâng cao vip pro"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Android"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Web"),
        Course(1, credits ="3 tín chỉ", name = "Lập trình Web"),
    )

    val sateLesson: List<String> = listOf(
        "--Chưa đến--",
        "Có mặt",
        "Vắng"
    )

    val assignment: List<Assignment> = listOf(
        Assignment(
            name = "Vẽ ui figma android app",
            endTime = "03/03/2025 14:00",
            state = StateAssignment.Overdue.name
        ),
        Assignment(
            name = "Vẽ ui figma android app",
            endTime = "03/03/2025 14:00",
            state = StateAssignment.Incomplete.name
        ),
        Assignment(
            name = "Vẽ ui figma android app",
            endTime = "03/03/2025 14:00",
            state = StateAssignment.Complete.name
        ),
        Assignment(
            name = "Vẽ ui figma android app",
            endTime = "03/03/2025 14:00",
            state = StateAssignment.Incomplete.name
        ),
        Assignment(
            name = "Vẽ ui figma android app",
            endTime = "03/03/2025 14:00",
            state = StateAssignment.Overdue.name
        ),
    )

    val lesson: List<Lesson> = listOf(
        Lesson(
            id = 0,
            name = "Thứ 4 ngày 03/03/2025",
            date = "Thứ 4, Ngày 03/03/2025",
            startTime = "9:00",
            endTime = "12:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Absent.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.NotYet.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Absent.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.NotYet.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Absent.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.NotYet.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        ),
        Lesson(
            id = 0,
            name = "Thứ 2 ngày 03/03/2025",
            date = "03/03/2025 14:00",
            startTime = "14:00",
            endTime = "17:00",
            state = StateLesson.Present.name,
            note = "12342",
            courseId = 1,
            location = "301-A2"
        )

    )
}