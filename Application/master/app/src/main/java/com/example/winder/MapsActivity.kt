package com.example.winder

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.winder.MainActivity.Companion.listOfWindParkData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_maps.*
import java.text.DecimalFormat

var hybrid = false

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.menu.findItem(R.id.navMap).isChecked = true
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navSettings -> {
                    val intent1 = Intent(this, SettingsActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0)
                }
                R.id.navWindParks -> {
                    val intent1 = Intent(this, WindParkCardActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0)
                }
            }
            true
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
        /**Sets the map type to the one chosen on the settings page
         * Default map is normal (roadmap)
         */
        if (hybrid) {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        } else {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        addMarkers()
        centerMap()
    }

    /**Centers the map to central norway, shifted two degrees west
     * No param, no return
     */
    private fun centerMap() {
        //Creates a variable to the geographical center of norway (shifted 2 degrees west)
        val centerNorway = LatLng(63.990556, 10.307778)
        //Center the map top norway and zooms
        val cp= CameraPosition.builder().bearing(0F).target(centerNorway).zoom(4.9F).build()
        val yourLocation = CameraUpdateFactory.newCameraPosition(cp)
        mMap.animateCamera(yourLocation)
    }

    /**@param city: coordinates to place to show
     * zooms in on given coordinates
     */
    private fun showPlace(city : LatLng) {
        val location = CameraUpdateFactory.newLatLngZoom(city, 12f)
        mMap.animateCamera(location)
    }

    fun addMarkers() {
        /**Adds a marker to the given coordinates
         * When a marker is pressed the map will zoom in on the given location
         */
        var lastMarkerPressed: Marker = mMap.addMarker(MarkerOptions().position(LatLng(0.0,0.0)).visible(false))
        val coordinates = ParkCoordinates()
        for (park in listOfWindParkData) {
            for ((name, c) in coordinates.parkCoordinates) {
                if (name == park.name) {
                    mMap.addMarker(MarkerOptions().position(LatLng(c[1], c[0])).title(park.name))
                }
            }
        }
        //Når en marker trykkes på, viser den stedet som har samme navn som markeren
        mMap.setOnMarkerClickListener { marker ->
            for (park in listOfWindParkData) {
                if (marker.title == park.name){
                    for ((name, c) in coordinates.parkCoordinates) {
                        if (name == park.name) {
                            marker.showInfoWindow()
                            showPlace(LatLng(c[1], c[0]))
                            lastMarkerPressed = marker
                        }
                    }
                }
            }
            true
        }

        var popup: PopupWindow = PopupWindow()

        //Viser kortet til stedet når man trykker på Info vinduet til markeren
        mMap.setOnInfoWindowClickListener {
            lateinit var item: WindParkData
            for (e in listOfWindParkData) {
                if (it.title == e.name) {
                    item = e
                }
            }
            //Show correct card
            popup = showPopup(item.name, item.capacity.toString(), item.county, item.hourlyData)
        }

        //Reseter kartet når du trykker på det
        mMap.setOnMapClickListener {
            lastMarkerPressed.hideInfoWindow()
            popup.dismiss()
            centerMap()
        }
    }

    /*fun notifyChanged() {
        addMarkers()
    }
*/
    private fun showPopup(name: String, capacity: String, county: String, hourlyData: ArrayList<WindData>): PopupWindow {
        val inflater: LayoutInflater = LayoutInflater.from(this.applicationContext) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_pop_up_window,null)
        val popup: PopupWindow = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val accurateFormat = DecimalFormat("0.00")
        val roundedFormat = DecimalFormat("0.0")
        val production = hourlyData[0].production
        val avgHouseholdHourlyCons = 16079/8760
        val co2ReductionConst = 256.3 - 20

        //Requires API level 23 or higher
        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popup.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.END
        popup.exitTransition = slideOut

        view.findViewById<TextView>(R.id.vindParkNavn).text = name
        view.findViewById<TextView>(R.id.maxKapasitet).text = "Installert effekt:\n$capacity MW"
        view.findViewById<TextView>(R.id.kommuneNavn).text = "Kommune:\n$county"
        view.findViewById<TextView>(R.id.naatidsProduksjon).text = "Produksjon:\n${accurateFormat.format(production)} MW"
        view.findViewById<TextView>(R.id.naatidsVindstyrke).text = "Vindstyrke:\n${hourlyData[0].windSpeed} m/s"
        view.findViewById<TextView>(R.id.sammenligning).text = "Denne parken forsyner nå ${roundedFormat.format(production/avgHouseholdHourlyCons)} husholdninger" +
                " og reduserer CO₂-utslippene med ${roundedFormat.format(production*co2ReductionConst/1000)} kg"


        view.findViewById<Button>(R.id.popup_window_button).setOnClickListener {
            popup.dismiss()
        }
        popup.showAtLocation(map, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER + 300)

        return popup
    }
}
