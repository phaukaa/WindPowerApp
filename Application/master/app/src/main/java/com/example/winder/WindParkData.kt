package com.example.winder

/**
 * Data class for storing information on windpower parks
 * @param name: String, Name of park
 * @param capacity: Double, Installed maximum production capacity of park
 * @param location: String, String representation of location - name of municipality
 * @param elspotArea: String, Specifies which el-price area the park lies in (affects income - not implemented)
 * @param county: String, Specifies county, for aggregationpurposes mainly (not implemented)
 * @param hourlyData: ArrayList<WindData>, Timestamped data on wind and production for park
 */
data class WindParkData(var name: String, var capacity: Double, var location: String, var elspotArea: String, var county: String, var hourlyData : ArrayList<WindData>)