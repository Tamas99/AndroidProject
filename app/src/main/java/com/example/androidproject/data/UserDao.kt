package com.example.androidproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table WHERE email LIKE :email AND password LIKE :password")
    suspend fun readOneData(email: String, password: String): User

    @Query("SELECT * FROM user_table WHERE status = 1")
    suspend fun getActive(): User

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

}