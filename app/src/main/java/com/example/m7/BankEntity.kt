package com.example.m7

import androidx.room.*

@Entity(tableName = "bank")
data class BankEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val user_id: Int,
    val name: String,
    val saldo: Int,
) {
    companion object {
        var listBank: MutableList<BankEntity> = mutableListOf()
    }

    override fun toString(): String {
        return name
    }
}