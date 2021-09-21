package com.example.winder


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class LocationForecastViewModel(private val locationForecastService : LocationForecastService) : ViewModel() {

    val locationForecastData = MutableLiveData<LocationForecastData>()
    var windInfo = mutableListOf<WindData>()
    var allParksWindForecast = HashMap<String, List<WindData>>()

    fun getWindData(name: String) : List<WindData>? {
        return allParksWindForecast.get(name)
    }

    fun fetchWindData(lat: String, lon: String, park: WindParkData) {
        val wind = mutableListOf<WindData>()

        viewModelScope.launch {
            val apiResponse = locationForecastService.fetchLocationForecastData(lat, lon)
            locationForecastData.value = apiResponse

            apiResponse.product.time.forEach {info ->

                if(info.location.windSpeed != null) {
                    val windForecast = info.location.windSpeed.mps
                    wind.add(WindData(info.to, info.from, windForecast, calculateProduction(windForecast, park.capacity)))
                }
            }
            windInfo = wind
            park.hourlyData = windInfo as ArrayList<WindData>
        }
    }

    /**
     * Method for calculating production, production is 0 when wind is over 25 or under 5
     * @param wind: Float, wind speed in m/s
     * @param capacity: Double, max production for park
     */
    fun calculateProduction(wind : Float, capacity: Double) :Double {
        if (wind >= 25 || wind <= 4) return 0.0
        if (wind > 18) return capacity
        return (wind/18)*capacity;
    }

    class InstanceCreator : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://in2000-apiproxy.ifi.uio.no")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service: LocationForecastService = retrofit.create(LocationForecastService::class.java)
            return modelClass.getConstructor(LocationForecastService::class.java).newInstance(service)
        }
    }
}