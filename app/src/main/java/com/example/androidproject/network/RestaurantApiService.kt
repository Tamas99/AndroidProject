package com.example.androidproject.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

private const val BASE_URL = "https://opentable.herokuapp.com/"

//5
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

//2
private val retrofit = Retrofit.Builder()
        //5
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        //8
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

//3
interface RestaurantApiService {
    @GET("api/restaurants?city=London")
    fun getProperties():
            //9
            Deferred<Restaurants>
}

//4
object RestaurantApi {
    val retrofitService: RestaurantApiService by lazy {
        retrofit.create(RestaurantApiService::class.java)
    }
}