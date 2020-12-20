package com.example.androidproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoriteRestaurant(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val restaurantName: String,
    val userid: Int
    )
