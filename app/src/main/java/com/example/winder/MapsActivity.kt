package com.example.winder

import android.os.Bundle
import android.view.View
import android.view.textclassifier.SelectionEvent
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        centerButton.setOnClickListener {
            centerMap()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /**Shows a switch that lets the user choose between default and hybrid map
         * Default map is normal (roadmap)
         */
        findViewById<Switch>(R.id.mapSwitch)?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            else {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        //Create variable with coordinates to oslo
        //***This must bo in a loop(?) when api is ok***
        val oslo = LatLng(59.911491, 10.757933)
        val bergen = LatLng(60.3925, 5.323333)

        /**Adds a marker to the given coordinates
         * When a marker is pressed the map will zoom in on the given location
         * (Will be done in a loop later when the api is good)
         */
        mMap.addMarker(MarkerOptions().position(oslo).title("Oslo"))
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
                showPlace(oslo)
            }
            true
        }

        /*
        val map = hashMapOf<String, LatLng>("oslo" to LatLng(59.911491, 10.757933), "bergen" to LatLng(60.3925, 5.323333))

        for ((key, value) in map) {
            mMap.addMarker(MarkerOptions().position(value).title(key))
            mMap.setOnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                } else {
                    marker.showInfoWindow()
                    showPlace(value)
                }
                true
            }
        }
        */

        centerMap()
    }

    /**Centers the map to central norway, shifted two degrees west
     * No param, no return
     */
    private fun centerMap() {
        //Creates a variable to the geographical center of norway (shifted 2 degrees west)
        val centerNorway = LatLng(63.990556, 10.307778)
        //Center the map top norway and zooms
        val yourLocation = CameraUpdateFactory.newLatLngZoom(centerNorway, 4.9f)
        mMap.animateCamera(yourLocation)
    }

    /**param: coordinates
     * zooms in on given coordinates
     */
    private fun showPlace(city : LatLng) {
        val location = CameraUpdateFactory.newLatLngZoom(city, 12f)
        mMap.animateCamera(location)
    }
}

