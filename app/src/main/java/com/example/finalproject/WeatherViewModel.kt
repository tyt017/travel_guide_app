package com.example.finalproject

import com.example.finalproject.weather.CityWeather
import com.example.finalproject.weather.GetService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    //Add a list of cities (cities) for selection
    val cities = listOf<String>("Taipei", "New York", "London", "Tokyo", "Kaohsiung City", "Hualien City")
    //Declare a LiveData
    val selectedCityWeather = MutableLiveData<CityWeather>()

    fun sendRetrofitRequest(location: String) {
        //CoroutineScope(Dispatchers.IO).launch {
        viewModelScope.launch {
            try {
                val result =
                //GetService.retrofitService.getAppData(location, "metric", "zh_tw", "API_KEY")
                //GetService.retrofitService.getAppData(location, "metric", "en", "API_KEY")
                    //GetService.retrofitService.getAppData(location, "metric", "en", "d03a9ba372344842261bce4113589cf8")
                    GetService.retrofitService.getAppData(location, "metric", "en", API_KEY)


                val cityWeather = CityWeather(
                    result.name,
                    result.main.temp,
                    result.main.temp_min,
                    result.main.temp_max,
                    result.main.humidity,
                    result.weather[0].main,
                    result.weather[0].description
                )

                selectedCityWeather.value = cityWeather
                
                Log.d("Main", cityWeather.toString())
            } catch (e: Exception) {
                Log.d("Main", "Fail to access: ${e.message}")
            }
        }
    }


    // class  object variables
    companion object {
        const val API_URL = "https://api.openweathermap.org/"
        const val API_KEY = "d03a9ba372344842261bce4113589cf8"

    }

}