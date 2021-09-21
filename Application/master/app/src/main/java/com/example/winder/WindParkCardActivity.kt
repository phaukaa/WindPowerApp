package com.example.winder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.winder.MainActivity.Companion.listOfWindParkData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_wind_park_card.*
import java.util.*

class WindParkCardActivity : AppCompatActivity() {

    private lateinit var listAdapt: ListAdapter

    var displayList: MutableList<WindParkData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wind_park_card)
        init()

        val searchBar = findViewById<SearchView>(R.id.search)
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapt.filter.filter(newText)
                return false
            }
        })

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.menu.findItem(R.id.navWindParks).isChecked = true
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navSettings -> {
                    val intent1 = Intent(this, SettingsActivity::class.java)
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

        recycler.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> hideKeyboard()
                MotionEvent.ACTION_UP -> hideKeyboard()
                MotionEvent.ACTION_BUTTON_PRESS -> hideKeyboard()
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //Initializes and fills the recycler view
    private fun init() {
        displayList.addAll(listOfWindParkData)
        recycler.apply {
            layoutManager = LinearLayoutManager(this@WindParkCardActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(15)
            addItemDecoration(topSpacingDecorator)
            listAdapt = ListAdapter(displayList)
            adapter = listAdapt
        }
    }

    //Initializes and creates a seachbar at the top menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        /*menuInflater.inflate(R.menu.windparkcard_searchbar,menu)*/
        /*val searchItem = menu.findItem(R.id.menu_search)*/

        findViewById<SearchView>(R.id.search)?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText!!.isNotEmpty()) {
                    displayList.clear()

                    val search = newText.toLowerCase(Locale.ROOT)

                    listOfWindParkData.forEach(){
                        if(it.name.toLowerCase(Locale.ROOT).contains(search) or it.location.toLowerCase(Locale.ROOT).contains(search) ){
                            displayList.add(it)

                        }
                        listAdapt.notifyDataSetChanged()
                    }
                } else{
                    displayList.clear()
                    displayList.addAll(listOfWindParkData)
                    listAdapt.notifyDataSetChanged()
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}
//Creates a bit of spacing between the cards
class TopSpacingItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.left = padding
        outRect.right = padding
    }
}
