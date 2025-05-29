package com.hiendao.eduschedule.ui.navigation

import androidx.annotation.StringRes
import com.hiendao.eduschedule.R

//  Phân tree cho màn hình
enum class TypeGraph(val route: String) {
    Auth("auth"),
    Home("home"),

    // navGraph for detail course
    Course("Course"),
}

enum class AppScreen(@StringRes val title: Int, val route: String) {
    HomeDetail(R.string.home_detail, route = "HomeDetail"),
    HomeTab(R.string.home, route = "HomeTab"),

    Utilities(R.string.utilities, route = "Utilities"),
    UtilsDetail(R.string.utilities_detail, route = "UtilsDetail"),
    Statistical(R.string.statistical, route = "Statistical"),

    Notification(R.string.notification, route = "Notification"),
    NotificationDetail(R.string.notification_detail, route = "NotificationDetail/{notiId}"),

    Setting(R.string.setting, route = "Setting"),
    Profile(R.string.profile, route = "Profile/{name}"),
    ChangePassword(R.string.change_password, route = "ChangePassword"),
    ChangeLanguage(R.string.change_language, route = "ChangeLanguage"),

    Login(R.string.login, route = "Login"),
    SignUp(R.string.sign_up, route = "SignUp"),
    ForgotPassword(R.string.forgot_password, route = "forgotPassword/{email}/{isUpdatePassword}"),
    VerifyEmail(R.string.verify_email, route = "VerifyEmail/{isChangePassword}"),

    Courses(R.string.courses, route = "Courses"),
    CourseDetail(R.string.detail_course, route = "CourseDetail/{courseId}"),
    LessonDetail(R.string.lesson, route = "LessonDetail/{lessonId}"),
    CourseInfo(R.string.course_info, route = "CourseInfo"),
    Assignment(R.string.assignment, route = "Assignment/{courseId}"),
    AssignmentDetail(R.string.assignment_detail, route = "AssignmentDetail/{assignmentId}"),

    Alarm(R.string.alarm, route = "Alarm"),
    AlarmDetail(R.string.alarm_detail, route = "AlarmDetail/{alarmId}")


}
