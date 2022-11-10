package com.example.m7

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user:UserEntity)

    @Delete
    suspend fun delete(user:UserEntity)

    @Query("DELETE FROM users where username = :username")
    suspend fun deleteQuery(username: String)

    @Query("SELECT * FROM users")
    suspend fun fetch():List<UserEntity>

    @Query("SELECT * FROM users where username = :username")
    suspend fun get(username:String):UserEntity?

}