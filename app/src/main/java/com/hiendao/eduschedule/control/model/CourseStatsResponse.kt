package com.hiendao.eduschedule.control.model

import com.hiendao.eduschedule.utils.entity.CourseStatsInformation
import com.squareup.moshi.Json

data class CourseStatsResponse(
    val timeStart: String? = "",
    val timeEnd: String? = "",
    val courseResponseDtoList: List<CourseStatsInformation> = emptyList(),
    val present: Int? = 0,
    val absent: Int? = 0,

    val apiPath: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val errorTime: String?
)
