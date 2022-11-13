package com.example.m7

import androidx.room.*

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val bank_id: Int,
    val bank_nama: String,
    val keterangan: String,
    val nominal: Int,
    val tanggal: String,
    val status: Int,
) {
    companion object {
        var listHistory: MutableList<HistoryEntity> = mutableListOf()
    }
    override fun toString(): String {
        return "$keterangan - $nominal - $tanggal - $status"
    }
}