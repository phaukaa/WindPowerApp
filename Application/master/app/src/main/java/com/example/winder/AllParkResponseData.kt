package com.example.winder

/**
 * Data class for storing information on windpower parks
 * @param KraftverksNavn: String, Name of park
 * @param MW: Double, Installed maximum production capacity of park
 * @param TypeAnlegg: String, Type of power plant (e.g "Vindkraft", "Vannkraft")
 * @param GWh: Double, Estimated yearly production
 * @param Kommune: String, String representation of location - name of municipality
 * @param Omraade: String, Specifies which el-price area the park lies in (affects income - not implemented)
 * @param Fylke: String, Specifies county, for aggregationpurposes mainly (not implemented)
 * @param hourlyData: ArrayList<WindData>, Timestamped data on wind and production for park
 */
data class AllParkResponseData (
    var TypeAnlegg: String,
    var KraftverksNavn: String,
    var GWh: Double,
    var MW: Double,
    var Fylke: String,
    var Kommune: String,
    var Omraade: String
)