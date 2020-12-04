package com.example.androidproject.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurants(val total_entries: String, val restaurants: List<Restaurant>) : Parcelable

@Parcelize
data class Restaurant(val id: String,
                      val name: String,
                      val address: String,
                      val city: String,
                      val state: String,
                      val area: String,
                      val postal_code: String,
                      val country: String,
                      val phone: String,
                      val lat: Float,
                      val lng: Float,
                      val price: String,
                      val reserve_url: String,
                      val mobile_reserve_url: String,
                      @Json(name="image_url") val imgSrcUrl: String) : Parcelable {
    override fun toString(): String {
        return "${name}\nPrice: ${price}$\n${city}, ${state}, ${country}\n\nAddress\n${address}\n\n${area}\n\nPostal code\n${postal_code}"
    }
                      }