package com.example.androidproject.data

class UserRepository(private val userDao: UserDao, private val favRestDao: FavRestDao, private val profilePictureDao: ProfilePictureDao) {

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

    fun getUserByEmail(email: String) : User? {
        return userDao.getUserByEmail(email)
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

    // Profile Picture
    suspend fun addProfilePicture(profilePicture: ProfilePicture) {
        profilePictureDao.addProfilePicture(profilePicture)
    }

    suspend fun getProfilePicture(user_id: Int) : ProfilePicture? {
        return profilePictureDao.getProfilePicture(user_id)
    }

    suspend fun deleteProfilePicture(user_id: Int) {
        profilePictureDao.deleteProfilePicture(user_id)
    }
}