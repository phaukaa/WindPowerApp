package com.example.winder


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WindParkDataViewModel(private val ParkDataService: ParkDataService) : ViewModel() {


    val parks = MutableLiveData<List<AllParkResponseData>>()
    val windParks = mutableListOf<WindParkData>()


    /**
     * Method for fetching data on power plants from NVE api
     * @Param windViewModel: LocationForecastViewModel, reference to to viewmodel for fetching wind data
     * */
    fun fetchPark(windViewModel : LocationForecastViewModel) {
        viewModelScope.launch {
            val dataResponse = ParkDataService.fetchParkData()

            parks.value = dataResponse
            extractWindParkData(windViewModel)
        }

    }

    /**
    * Method for filtering out non-wind power plants
    * @Param windViewModel: LocationForecastViewModel, reference to to viewmodel for fetching wind data
    * */
    fun extractWindParkData(windViewModel : LocationForecastViewModel) {
        val coordinatesObject = ParkCoordinates()
        val coordinates : HashMap<String, ArrayList<Double>> = coordinatesObject.parkCoordinates

        parks.value?.forEach { parkData ->

            if (parkData.TypeAnlegg == "Vindkraft") {
                val windParkCoordinates : ArrayList<Double>? = coordinates.get(parkData.KraftverksNavn)
                if (windParkCoordinates != null) {

                    var wind = arrayListOf(WindData("Error", "Error", 0.0.toFloat(), 0.0))

                    val park = WindParkData(
                        parkData.KraftverksNavn,
                        parkData.MW,
                        parkData.Kommune,
                        parkData.Omraade,
                        parkData.Fylke,
                        wind
                    )
                    windQuery(windViewModel, windParkCoordinates, park)
                    windParks.add(park)
                }
            }
        }
    }

    /**
     * Get()-method
     */
    fun getParks() : List<WindParkData> {
        return windParks
    }

    /**
     * Function for calling weather api for specific location
     * @param windViewModel: LocationForecastViewModel, reference to to viewmodel for fetching wind data
     * @param coordinates: ArrayList<Double>, list of coordnates assosiated with specific park
     * @param park: WindParkData, windpark instance
     */
    private fun windQuery(windViewModel: LocationForecastViewModel, coordinates : ArrayList<Double>, park: WindParkData) {
        windViewModel.fetchWindData(coordinates[1].toString(), coordinates[0].toString(), park)
    }

    class InstanceCreator : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) : T {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.nve.no/web/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service: ParkDataService = retrofit.create(ParkDataService::class.java)
            return modelClass.getConstructor(ParkDataService::class.java).newInstance(service)
        }
    }
}