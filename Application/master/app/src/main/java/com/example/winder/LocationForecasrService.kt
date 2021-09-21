package com.example.winder

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationForecastService {
    @GET("/weatherapi/locationforecast/1.9/.json?")
    suspend fun fetchLocationForecastData(
        @Query("lat") lat : String,
        @Query("lon") lon : String
    ) : LocationForecastData
}