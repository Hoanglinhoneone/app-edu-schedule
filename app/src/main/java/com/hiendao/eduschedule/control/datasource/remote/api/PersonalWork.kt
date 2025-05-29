package com.hiendao.eduschedule.control.datasource.remote.api

import com.squareup.moshi.Json

data class PersonalWork(
    @field:Json(name = "create_at")
    val createAt: String,
    val description: String,
    val id: Int,
    val name: String,
    val repeatCycle: String,
    val timeEnd: String,
    val timeStart: String,
    val userId: Int,
    val validTimeRange: Boolean,
    val workAddress: String
)