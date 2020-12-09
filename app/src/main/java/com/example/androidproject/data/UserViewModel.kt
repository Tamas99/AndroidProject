package com.example.androidproject.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidproject.network.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<User>>
    private val repository: UserRepository
    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    fun getUser(): LiveData<User> = user

    private var allFav: List<FavoriteRestaurant> = ArrayList()
    fun getAllFavsByUser() = allFav

    // Favorite Clicked
    private val favRestaurant: MutableLiveData<FavoriteRestaurant> by lazy {
        MutableLiveData<FavoriteRestaurant>()
    }
    fun getFavRest(): LiveData<FavoriteRestaurant> = favRestaurant

    // Favs by user
    private val favsByUser: MutableLiveData<List<FavoriteRestaurant>> by lazy {
        MutableLiveData<List<FavoriteRestaurant>>()
    }
    fun getFavsByUser(): LiveData<List<FavoriteRestaurant>> = favsByUser

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        val favRestDao = UserDatabase.getDatabase(application).favRestDao()
        repository = UserRepository(userDao, favRestDao)
        readAllData = repository.readAllData
        favRestaurant.value = FavoriteRestaurant(0,"none",1)
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun readOneData(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userdata = repository.readOneData(email, password)
            try {
                if (userdata.email?.compareTo(email) == 0 &&
                    userdata.password?.compareTo(password) == 0) {
                    val updateUser = User(userdata.id, userdata.username, userdata.password, userdata.email, true)
                    repository.updateUser(updateUser)
                    user.postValue(updateUser)
                }
            }catch (e: Exception) {}
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun getActive() {
        viewModelScope.launch(Dispatchers.IO) {
            var userdata: User? = repository.getActive()
            if (userdata == null) {
                userdata = User(0,"Guest", "none", "none", false)
            }
            user.postValue(userdata)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun addFav(favoriteRestaurant: FavoriteRestaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFav(favoriteRestaurant)
        }
    }

    fun getFav(restaurantName: String, userid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var restaurant: FavoriteRestaurant? = repository.getFav(restaurantName, userid)
            if (restaurant == null) {
                Log.d("Tag", "Null from room ${restaurantName!!} ${user.value!!.id}")
                Log.d("Tag", "${getAllFav()}")
                restaurant = FavoriteRestaurant(0, "none", 1)
            }
            favRestaurant.postValue(restaurant)
        }
    }

    fun getAllFav(): List<FavoriteRestaurant> {
        viewModelScope.launch(Dispatchers.IO) {
            allFav = repository.getAllFav()
        }
        return allFav
    }

    fun getFavsByUser(userid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val listRes: List<FavoriteRestaurant>? = repository.getFavsByUser(userid)
            favsByUser.postValue(listRes)
        }
    }

    fun deleteFav(favoriteRestaurant: FavoriteRestaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFav(favoriteRestaurant)
        }
    }

    fun deleteAllFav() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFav()
        }
    }

    fun onFavoriteClicked(restaurantName: String?) {
        try {
            getFav(restaurantName!!, user.value!!.id)
        } catch (e: NullPointerException) {
            Log.d("Tag", "NullPointer ${restaurantName!!} ${user.value!!.id}")
        }
    }

    override fun toString(): String {
        return readAllData.value.toString()
    }
}