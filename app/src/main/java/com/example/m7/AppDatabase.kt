package com.example.m7

import android.content.Context
import androidx.room.*

@Database(entities = [UserEntity::class, BankEntity::class, HistoryEntity::class], version = 1)
//@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val bankDao: BankDao
    abstract val historyDao: HistoryDao

    companion object {
        lateinit var db: AppDatabase

        fun build(context: Context) {
            db = Room.databaseBuilder(context,AppDatabase::class.java,"tutorw7_database").build()
        }
    }
}