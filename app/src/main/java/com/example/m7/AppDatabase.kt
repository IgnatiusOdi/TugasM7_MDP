package com.example.m7

import android.content.Context
import androidx.room.*

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        lateinit var db: AppDatabase
        private var _database: AppDatabase? = null

        fun build(context: Context?): AppDatabase {
            if(_database == null){
                _database = Room.databaseBuilder(context!!,AppDatabase::class.java,"tutorw7_database").build()
            }
            return _database!!
        }
    }
}