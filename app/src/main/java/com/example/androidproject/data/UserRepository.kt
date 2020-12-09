package com.example.androidproject.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao, private val favRestDao: FavRestDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun readOneData(email: String, password: String): User {
        return userDao.readOneData(email, password)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun getActive(): User {
        return userDao.getActive()
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    // Favorite Restaurant
    suspend fun addFav(favoriteRestaurant: FavoriteRestaurant) {
        favRestDao.addFav(favoriteRestaurant)
    }

    suspend fun getFav(restaurantName: String, userid: Int): FavoriteRestaurant {
        return favRestDao.getFav(restaurantName, userid)
    }

    suspend fun getAllFav(): List<FavoriteRestaurant> {
        return favRestDao.getAllFav()
    }

    suspend fun getFavsByUser(userid: Int): List<FavoriteRestaurant> {
        return favRestDao.getFavsByUser(userid)
    }

    suspend fun deleteFav(favoriteRestaurant: FavoriteRestaurant) {
        favRestDao.deleteFav(favoriteRestaurant)
    }

    suspend fun deleteAllFav() {
        favRestDao.deleteAllFav()
    }
}