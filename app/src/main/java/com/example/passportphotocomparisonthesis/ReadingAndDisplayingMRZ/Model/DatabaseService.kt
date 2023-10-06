package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserBAC::class], version = 1)
abstract class DatabaseService: RoomDatabase() {

    abstract fun userBACDao(): UserBACDao

    companion object {

        private const val DATABASE_NAME = "user_bac.db"

        private var instance: DatabaseService? = null

        private fun create(context: Context): DatabaseService =
            Room.databaseBuilder(context, DatabaseService::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()


        fun getInstance(context: Context): DatabaseService =
            (instance ?: create(context)).also { instance = it }
    }
}