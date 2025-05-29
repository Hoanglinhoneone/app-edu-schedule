package com.hiendao.eduschedule.control.datasource.remote.api.personalWork

import com.hiendao.eduschedule.control.datasource.remote.api.PersonalWork

data class PersonalWorkResponse(
    val personalWork: PersonalWork?,
    val personalWorks: List<PersonalWork>?,
    val apiPath: String?,
    val errorCode: Int?,
    val errorMessage: String?,
    val errorTime: String?
)
