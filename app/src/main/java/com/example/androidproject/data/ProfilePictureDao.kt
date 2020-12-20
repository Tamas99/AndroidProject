package com.example.androidproject.data

import androidx.room.*

@Dao
interface ProfilePictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfilePicture(profilePicture: ProfilePicture)

    @Query("Select * from profilepicture_table where user_id = :user_id")
    suspend fun getProfilePicture(user_id: Int) : ProfilePicture

    @Query("Delete from profilepicture_table where user_id = :user_id")
    suspend fun deleteProfilePicture(user_id: Int)

}