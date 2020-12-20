package com.example.androidproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profilepicture_table")
data class ProfilePicture(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val user_id: Int,
    var image_uri: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfilePicture

        if (id != other.id) return false
        if (user_id != other.user_id) return false
        if (!image_uri.contentEquals(other.image_uri)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + user_id
        result = 31 * result + image_uri.contentHashCode()
        return result
    }
}
