
package com.example.androidproject.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject.detail.DetailFragment
import com.example.androidproject.network.Restaurants
import com.example.androidproject.network.Restaurant

/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(restaurant: Restaurant, app: Application) : AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<Restaurant>()
    val selectedProperty: LiveData<Restaurant>
        get() = _selectedProperty
    init {
        _selectedProperty.value = restaurant
    }
}
