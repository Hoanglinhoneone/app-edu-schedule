package com.hiendao.eduschedule.ui.screen.notification

data class NotificationItem(
    val id: Int = 0,
    val name: String = "",
    val content: String = "",
    val timeNoti: String = "",
    val isRead: Boolean = false,
    val notiType: String = ""
)

val listNotificationItems = listOf(
    NotificationItem(
        id = 1,
        name = "Thông báo hệ thống",
        content = "Hệ thống sẽ bảo trì vào lúc 22:00 tối nay.",
        timeNoti = "21:00 01/10/2023",
        isRead = false,
        notiType = "System"
    ),
    NotificationItem(
        id = 2,
        name = "Cập nhật ứng dụng",
        content = "Phiên bản mới đã được phát hành. Hãy cập nhật ngay!",
        timeNoti = "10:00 02/10/2023",
        isRead = true,
        notiType = "Update"
    ),
    NotificationItem(
        id = 3,
        name = "Lịch học mới",
        content = "Lịch học tuần này đã được cập nhật. Vui lòng kiểm tra.",
        timeNoti = "08:00 03/10/2023",
        isRead = false,
        notiType = "Schedule"
    ),
    NotificationItem(
        id = 4,
        name = "Khuyến mãi đặc biệt",
        content = "Nhận ưu đãi 20% khi đăng ký khóa học mới trong hôm nay.",
        timeNoti = "12:00 04/10/2023",
        isRead = true,
        notiType = "Promotion"
    ),
    NotificationItem(
        id = 5,
        name = "Nhắc nhở",
        content = "Bạn có một bài kiểm tra vào ngày mai. Hãy chuẩn bị kỹ lưỡng.",
        timeNoti = "15:00 05/10/2023",
        isRead = false,
        notiType = "Reminder"
    )
)