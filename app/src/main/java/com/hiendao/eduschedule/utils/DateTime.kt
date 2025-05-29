package com.hiendao.eduschedule.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.time.ZoneOffset

fun Long.convertMillisToDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun getStartOfDayTimestamp(): Long {
    // Lấy thời gian hiện tại
    val currentTimeMillis = System.currentTimeMillis()

    // Tạo một đối tượng Calendar từ thời gian hiện tại
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTimeMillis

    // Đặt giờ, phút, giây, và mili giây về 0
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Trả về timestamp cho thời gian bắt đầu của ngày
    return calendar.timeInMillis
}

fun getDayOfWeek(timeInMillis: Long): Int{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)  // 1: Sunday, 2: Monday, ..., 7: Saturday

    // Chuyển đổi sang dạng 0-6, với Thứ Hai là 0
    val adjustedDay = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
    return adjustedDay
}

fun getMonthYearFromMillis(timeInMillis: Long): String {
    // Định dạng để lấy "Tháng 1 - 2025"
    val sdf = SimpleDateFormat("MMMM - yyyy", Locale.getDefault())

    // Chuyển thời gian millis thành đối tượng Date
    val date = Date(timeInMillis)

    // Định dạng ngày tháng theo kiểu "Tháng 1 - 2025"
    val formattedDate = sdf.format(date)

    // Chuyển chữ cái đầu tiên của tên tháng thành chữ hoa
    return formattedDate.replaceFirstChar { it.uppercase() }
}

// formater : dd/MM/yyyyTHH:mm:ss
@SuppressLint("NewApi")
fun String.convertToTimestamp(): Long {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)
    val timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    return timestamp
}

fun String.convertDateToMillis(formater: String): Long {
    val formatter = SimpleDateFormat(formater, Locale.getDefault())
    return formatter.parse(this)?.time ?: 0
}

fun String.checkTime(): String = if (this.length == 1)  "0$this" else this

fun formatTime(hour: Int, minute: Int): String {
    return String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
}

fun String.convertToTimeInMillis(): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(this)
    return date?.time ?: 0L
}

fun Long.toDateAndTimeString(): Pair<String, String>{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val dateString = dateFormat.format(Date(this))
    val timeString = timeFormat.format(Date(this))

    return Pair(dateString, timeString)
}

@SuppressLint("NewApi")
fun checkValidTime(
    startTime: String,
    endTime: String,
    timePattern: String = "HH:mm"
): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(timePattern)
        val startTime = LocalTime.parse(startTime, formatter)
        val endTime = LocalTime.parse(endTime, formatter)

        when {
            !startTime.isBefore(endTime) -> {
                Timber.e("Giờ bắt đầu ($startTime) phải trước giờ kết thúc ($endTime)")
                false
            }
            else -> true
        }
    } catch (e: Exception) {
        Timber.e(" Lỗi định dạng giờ: ${e.message}")
        false
    }
}

@SuppressLint("NewApi")
fun checkValidDate(
    startDay: String,
    endDay: String,
    datePattern: String = "dd/MM/yyyy"
): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(datePattern)
        val startDate = LocalDate.parse(startDay, formatter)
        val endDate = LocalDate.parse(endDay, formatter)

        when {
            startDate.isAfter(endDate) -> {
                Timber.e("Ngày bắt đầu ($startDay) phải trước ngày kết thúc ($endDay)")
                false
            }
            else -> true
        }
    } catch (e: Exception) {
        Timber.e("Lỗi định dạng ngày: ${e.message}")
        false
    }
}


@SuppressLint("NewApi")
fun checkValidDateTime(endDay: String, endTime: String): Boolean {
    Timber.i("checkValidDateTime: $endDay $endTime")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val dateTimeStr = "$endDay $endTime"
    val endDateTime = LocalDateTime.parse(dateTimeStr, dateFormatter)
    val currentDateTime = LocalDateTime.now()

    return currentDateTime.plusMinutes(1).isBefore(endDateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFirstDayOfCurrentMonth(): String {
    val firstDayOfMonth = LocalDate.now().withDayOfMonth(1)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'00:00:00")
    return firstDayOfMonth.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getLastDayOfCurrentMonth(): String {
    val lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'23:59:59")
    return lastDayOfMonth.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentMonth(): Int {
    return LocalDate.now().monthValue
}

fun String.getDay() : String {
    Timber.i("convert $this to day format:dd/MM/yyyy")
    return this.split("T")[0]
}

fun String.getTime() : String {
    Timber.i("convert $this to time format: HH:mm")
    val timer = this.split("T")[1].split(":")
    return "${timer[0]}:${timer[1]}"
}

fun combineDate(date: String, time: String): String {
    Timber.i(" result combine data: ${date}T${time}:00")
    return "${date}T${time}:00"
}

fun String.getTimeDeadline(): String {
    return "${this.getDay()} ${this.getTime()}"
}

