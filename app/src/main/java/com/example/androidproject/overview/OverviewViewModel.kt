package com.example.androidproject.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject.network.Restaurant
import com.example.androidproject.network.RestaurantApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
//import javax.security.auth.callback.Callback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class RestaurantApiStatus { LOADING, ERROR, DONE }


/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    //12
    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<RestaurantApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<RestaurantApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<Restaurant>>()

    val properties: LiveData<List<Restaurant>>
    get() = _properties

    // Navigate
    private val _navigateToSelectedProperty = MutableLiveData<Restaurant>()
    val navigateToSelectedProperty: LiveData<Restaurant>
        get() = _navigateToSelectedProperty

    //10
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    /**
     * Call getRestaurantProperties() on init so we can display status immediately.
     */
    init {
        getRestaurantProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getRestaurantProperties() {
        //5
        //7
        //11
        coroutineScope.launch {
            var getPropertiesDeferred = RestaurantApi.retrofitService.getProperties()
            try {
                _status.value = RestaurantApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = RestaurantApiStatus.DONE
                _properties.value = listResult.restaurants
            } catch (e: Exception) {
                _status.value = RestaurantApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun displayPropertyDetails(restaurant: Restaurant) {
        _navigateToSelectedProperty.value = restaurant
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}
