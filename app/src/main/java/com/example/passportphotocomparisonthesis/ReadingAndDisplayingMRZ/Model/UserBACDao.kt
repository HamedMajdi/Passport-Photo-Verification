package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.util.query

@Dao
interface UserBACDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userBAC: UserBAC)

    @Query(value = "SELECT * FROM user_bac WHERE document_id = :id")
    suspend fun getUser(id: String): UserBAC?

    @Query("SELECT * FROM user_bac")
    suspend fun getAllUsers(): List<UserBAC>

    @Delete
    suspend fun deleteUser(userBAC: UserBAC)
}