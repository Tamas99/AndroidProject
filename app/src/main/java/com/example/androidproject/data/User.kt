package com.example.androidproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val username: String,
    val password: String,
    val email: String,
    var status: Boolean
)