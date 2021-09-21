package com.example.winder
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {
    //Global list of WindParkData objects to be used in multiple activities
    companion object {
        lateinit var listOfWindParkData : MutableList<WindParkData>
    }

    private val viewModel: WindParkDataViewModel by viewModels { WindParkDataViewModel.InstanceCreator() }
    private val locationForecastViewModel : LocationForecastViewModel by viewModels {LocationForecastViewModel.InstanceCreator() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoroutineScope(IO).async {
            fetchWindData()
        }

        //Starter map activity med en gang appen åpnes
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
    }

    private fun fetchWindData() {
        viewModel.fetchPark(locationForecastViewModel)
        listOfWindParkData = viewModel.getParks() as MutableList<WindParkData>
        //MapsActivity().notifyChanged()
        MapsActivity().addMarkers()
    }
}
