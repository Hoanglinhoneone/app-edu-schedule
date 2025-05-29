package com.hiendao.eduschedule.utils

object AppConfig {
    var lastClickTime: Long = 0
    const val DOUBLE_CLICK_TIME_DELTA: Long = 600
    fun isDoubleClick(): Boolean {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            return true
        }
        lastClickTime = clickTime
        return false
    }
}