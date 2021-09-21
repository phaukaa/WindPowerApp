package com.example.winder

/**
 * Data class for storing information on windpower parks
 * @param to: String, Starttime for forecast
 * @param to: String, End time for forecast
 * @param windspeed: Float, average windspeed for given timestamp
 * @param production: Double, calculated production for given timestamp
 */
data class WindData(val to : String, val from : String, val windSpeed : Float, var production : Double = 0.0)