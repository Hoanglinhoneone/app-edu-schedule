package com.hiendao.eduschedule.control.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface EventDao {
    /* **********************************************************************
     * Function
     ********************************************************************** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert()

    // TODO: create
}