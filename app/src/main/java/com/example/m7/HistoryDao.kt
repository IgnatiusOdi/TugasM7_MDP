package com.example.m7

import androidx.room.*

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history:HistoryEntity)

    @Update
    suspend fun update(history:HistoryEntity)

    @Delete
    suspend fun delete(history:HistoryEntity)

    @Query("DELETE FROM history where id = :id")
    suspend fun deleteQuery(id: Int)

    @Query("SELECT * FROM history")
    suspend fun fetch():List<HistoryEntity>

    @Query("SELECT * FROM history where bank_id = :bank_id")
    suspend fun get(bank_id:Int):List<HistoryEntity>
}