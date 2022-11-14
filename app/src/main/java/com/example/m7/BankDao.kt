package com.example.m7

import androidx.room.*

@Dao
interface BankDao {
    @Insert
    suspend fun insert(bank:BankEntity)

    @Update
    suspend fun update(bank:BankEntity)

    @Delete
    suspend fun delete(bank:BankEntity)

    @Query("DELETE FROM bank where id = :id")
    suspend fun deleteQuery(id: Int)

    @Query("SELECT * FROM bank")
    suspend fun fetch():List<BankEntity>

    @Query("SELECT * FROM bank where user_id = :user_id")
    suspend fun get(user_id:Int):List<BankEntity>

    @Query("SELECT * FROM bank where id = :bank_id")
    suspend fun getBank(bank_id:Int):BankEntity?
}