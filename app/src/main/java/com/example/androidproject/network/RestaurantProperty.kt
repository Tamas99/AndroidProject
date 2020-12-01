package com.example.androidproject.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurants(var total_entries: String, var restaurants: List<Restaurant>) : Parcelable

@Parcelize
data class Restaurant(var id: String,
                      var name: String,
                      var address: String,
                      var city: String,
                      var state: String,
                      var area: String,
                      var postal_code: String,
                      var country: String,
                      var phone: String,
                      var lat: Float,
                      var lng: Float,
                      var price: Float,
                      var reserve_url: String,
                      var mobile_reserve_url: String,
                      @Json(name="image_url") var imgSrcUrl: String) : Parcelable