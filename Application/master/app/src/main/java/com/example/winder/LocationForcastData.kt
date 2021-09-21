package com.example.winder

data class LocationForecastData(val product : Product)

data class Product(val time : List<Time>)

data class Time(val location : Location, val to : String, val from : String)

data class Location(val windSpeed : WindSpeed?)

data class WindSpeed(val id : String?, val beaufort : Int?, val name : String?, val mps : Float)