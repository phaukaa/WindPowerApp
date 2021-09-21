package com.example.winder

import retrofit2.http.GET

interface ParkDataService {

    @GET("ElCert/GetApplications")
    suspend fun fetchParkData(): List<AllParkResponseData>
}