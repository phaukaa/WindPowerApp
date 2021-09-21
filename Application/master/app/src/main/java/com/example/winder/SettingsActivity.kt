package com.example.winder

import android.content.Intent
import android.graphics.Typeface.*
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupHyperLink()

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.menu.findItem(R.id.navSettings).isChecked = true
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navWindParks -> {
                    val intent1 = Intent(this, WindParkCardActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0)
                }
                R.id.navMap -> {
                    val intent1 = Intent(this, MapsActivity::class.java)
                    startActivity(intent1)
                    overridePendingTransition(0, 0)
                }
            }
            true
        }

        /**
         *  Sets the switch to true if the map is hybrid, else false
         *  Listens to the switch and sets the mapType
         */
        val hybridMapSwitch = findViewById<Switch>(R.id.mapSwitch)
        hybridMapSwitch?.isChecked = hybrid
        hybridMapSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {hybrid = true}
            if (!isChecked) {hybrid = false}
        }

        // show about info
        val aboutButton = findViewById<Button>(R.id.team24InfoButton)
        val aboutTextView = findViewById<TextView>(R.id.team24Info)
        aboutButton.setOnClickListener { buttonClicked(aboutButton, aboutTextView) }

        // show data protection info
        val dataProtectionButton = findViewById<Button>(R.id.personvernInfoButton)
        val dataProtectionTextView = findViewById<TextView>(R.id.personvernInfo)
        dataProtectionButton.setOnClickListener { buttonClicked(dataProtectionButton, dataProtectionTextView) }

        // show availability info
        val availabilityButton = findViewById<Button>(R.id.tilgjengelighetInfoButton)
        val availabilityTextView = findViewById<TextView>(R.id.tilgjengelighetInfo)
        availabilityButton.setOnClickListener { buttonClicked(availabilityButton, availabilityTextView) }
    }

    /**
     * @param text: The TextView corresponding to the button.
     * Shows/hides the text.
     */
    private fun buttonClicked(button : Button, text: TextView) {
        if (text.visibility == View.GONE) {
            text.visibility = View.VISIBLE
            button.typeface = DEFAULT_BOLD
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.button_arrow_up, 0)

        } else if (text.visibility == View.VISIBLE) {
            text.visibility = View.GONE
            button.typeface = DEFAULT
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.button_arrow_down, 0)
        }
    }

    // Sets up clickable link(s).
    private fun setupHyperLink() {
        val linkDifi = findViewById<TextView>(R.id.tilgjengelighetInfo)
        linkDifi.movementMethod = LinkMovementMethod.getInstance()
    }
}