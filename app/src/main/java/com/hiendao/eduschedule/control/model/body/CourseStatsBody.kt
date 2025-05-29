package com.hiendao.eduschedule.control.model.body

import com.squareup.moshi.Json

data class CourseStatsBody (
    @field:Json(name = "timeStart")
    val timeStart: String = "28/04/2025T00:00:00",
    @field:Json(name = "timeEnd")
    val timeEnd: String = "30/07/2025T00:00:00"
)