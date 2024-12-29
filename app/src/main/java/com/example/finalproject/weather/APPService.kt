package com.example.finalproject.weather

import com.example.finalproject.WeatherViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Define a service interface

interface AppService {
    @GET("data/2.5/weather?")
    suspend fun getAppData(
        @Query("q") location: String, @Query("units") unit: String,
        @Query("lang") lang: String, @Query("appid") api_key: String
    ): WeatherData

}

//Create a Moshi converter object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Create a Retrofit object
private val retrofit = Retrofit.Builder()
    //.baseUrl("https://api.openweathermap.org/")    //API_URL => "https://api.openweathermap.org/"
    .baseUrl(WeatherViewModel.API_URL)    //API_URL => "https://api.openweathermap.org/"
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


//Create a singleton object to call the AppService
object GetService {
    val retrofitService : AppService by lazy {
        retrofit.create(AppService::class.java) }
}