package com.example.m7

import androidx.room.*

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val username: String,
    val password: String,
) {
    companion object {
        var userLoggedIn: UserEntity? = null
        var listUser: MutableList<UserEntity> = mutableListOf()
    }

    override fun toString(): String {
        return "$id"
    }
}