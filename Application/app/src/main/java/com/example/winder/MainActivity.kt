package com.example.winder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Starter map activity med en gang appen åpnes
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
