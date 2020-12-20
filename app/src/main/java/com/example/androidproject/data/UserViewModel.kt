package com.example.androidproject.data

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidproject.network.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.NullPointerException

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val repository: UserRepository
    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    fun getUser(): LiveData<User> = user

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

    // Profile Picture
    private val _profilePicture: MutableLiveData<ProfilePicture> by lazy {
        MutableLiveData<ProfilePicture>()
    }
    fun getImageUri() : LiveData<ProfilePicture> = _profilePicture

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        val favRestDao = UserDatabase.getDatabase(application).favRestDao()
        val profilePictureDao = UserDatabase.getDatabase(application).profilePictureDao()
        repository = UserRepository(userDao, favRestDao, profilePictureDao)
        user.value = User(0,"none", "none", "none", false)
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
            }catch (e: Exception) {
                Log.e("TagReadUser", e.toString())
            }
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
                restaurant = FavoriteRestaurant(0, "none", 1)
            }
            favRestaurant.postValue(restaurant)
        }
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

    fun onFavoriteClicked(restaurantName: String?, active: Boolean) {
        if (active == false) {
            Toast.makeText(this.getApplication(), "Please log in to make it favorite", Toast.LENGTH_LONG).show()
            return
        }
        try {
            getFav(restaurantName!!, user.value!!.id)
        } catch (e: NullPointerException) {
            Log.d("Tag", "NullPointer ${restaurantName!!} ${user.value!!.id}")
        }
    }

    // Profile Picture
    fun addProfilePicture(profilePicture: ProfilePicture) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProfilePicture(profilePicture)
        }
    }

    fun getProfilePicture(user_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var profilePicture: ProfilePicture?
            try {
                profilePicture = repository.getProfilePicture(user_id)
            } catch (e: IllegalStateException) {
                Log.e("TagIllegalState", e.toString())
                return@launch
            }
            if (profilePicture != null) {
                _profilePicture.postValue(profilePicture)
            }
        }
    }

    fun deleteProfilePicture(user_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProfilePicture(user_id)
        }
    }
}