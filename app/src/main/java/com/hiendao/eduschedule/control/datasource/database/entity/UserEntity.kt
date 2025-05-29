package com.hiendao.eduschedule.control.database.entity

import androidx.room.Entity

@Entity(tableName = "user")
data class UserEntity(
    val id: Int,
    val username: String?,
    val email: String,
    val fullName: String?,
    val phoneNumber: String?,
    val age: String?,
    val gender: String?,
    val dateOfBirth: String?,
)
