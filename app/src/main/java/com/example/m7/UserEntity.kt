package com.example.m7

import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val name: String,
    val username: String,
    val password: String,
) {
    companion object {
        lateinit var userLoggedIn: UserEntity
        var users: MutableList<UserEntity> = mutableListOf()
    }

    override fun toString(): String {
        return "$id"
    }
}