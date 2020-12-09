package com.example.androidproject.data

import androidx.room.*

@Dao
interface FavRestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFav(favoriteRestaurant: FavoriteRestaurant)

    @Query("select * from favorites_table where restaurantName like :restaurantName and userid = :userid")
    suspend fun getFav(restaurantName: String, userid: Int): FavoriteRestaurant

    @Query("select * from favorites_table")
    suspend fun getAllFav(): List<FavoriteRestaurant>

    @Query("select * from favorites_table where userid = :userid")
    suspend fun getFavsByUser(userid: Int): List<FavoriteRestaurant>

    @Delete
    suspend fun deleteFav(favoriteRestaurant: FavoriteRestaurant)

    @Query("delete from favorites_table")
    suspend fun deleteAllFav()

}