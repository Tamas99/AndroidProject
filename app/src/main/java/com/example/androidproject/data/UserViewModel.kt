package com.example.androidproject.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<User>>
    private val repository: UserRepository
    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    fun getUser(): LiveData<User> = user

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
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
            var userdata = repository.getActive()
            if (userdata == null) {
                userdata = User(0,"Guest", "none", "none", false)
            }
            user.postValue(userdata)
        }
    }
}