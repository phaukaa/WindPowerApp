package com.example.winder

import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.wind_card.view.*
import java.text.DecimalFormat
import java.util.*

var activePopup: PopupWindow = PopupWindow()

class ListAdapter (list: MutableList<WindParkData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var items: MutableList<WindParkData> = list

    private var filterItems: MutableList<WindParkData> = mutableListOf()

    init {
        filterItems = items
    }


    class ViewHolder constructor(private val card: View): RecyclerView.ViewHolder(card)  {

        private val cardTopText = card.parkNavn
        private val cardMiddleText = card.vindstyrke
        private val cardBottomText = card.kommune
        private val picture = card.bildeVind


        fun bind(windPark: WindParkData) {

            cardTopText?.text = windPark.name
            cardMiddleText?.text = "Kommune:\n${windPark.location.toLowerCase().capitalize()}"
            cardBottomText?.text = "Installert effekt:\n${windPark.capacity} MW"
            //picture?.setImageDrawable(ContextCompat.getDrawable(card.context, R.drawable.windr_icon_white))
            if (windPark.hourlyData[0].windSpeed < 5) {
                picture?.setImageDrawable(
                    ContextCompat.getDrawable(
                        card.context,
                        R.drawable.windr_wind_1
                    )
                )
            }
            else if (windPark.hourlyData[0].windSpeed < 10) {
                picture?.setImageDrawable(
                    ContextCompat.getDrawable(
                        card.context,
                        R.drawable.windr_wind_2
                    )
                )
            }
            else {
                picture?.setImageDrawable(
                    ContextCompat.getDrawable(
                        card.context,
                        R.drawable.windr_wind_3))
            }

            //When a card is presses, a new activity launches on top (hopefully) being a pop-up card
            card.setOnClickListener {
                activePopup.dismiss()
                activePopup = showPopup(windPark.name, windPark.capacity.toString(), windPark.location, windPark.hourlyData)
            }
        }

        private fun showPopup(name: String, capacity: String, location: String, hourlyData: ArrayList<WindData>): PopupWindow {
            val inflater: LayoutInflater = LayoutInflater.from(card.context) as LayoutInflater
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
            view.findViewById<TextView>(R.id.kommuneNavn).text = "Kommune:\n$location"
            view.findViewById<TextView>(R.id.naatidsProduksjon).text = "Produksjon:\n${accurateFormat.format(production)} MW"
            view.findViewById<TextView>(R.id.naatidsVindstyrke).text = "Vindstyrke:\n${hourlyData[0].windSpeed} m/s"
            view.findViewById<TextView>(R.id.sammenligning).text = "Denne parken forsyner nå ${roundedFormat.format(production/avgHouseholdHourlyCons)} husholdninger" +
                    " og reduserer CO\u2082-utslippene med ${roundedFormat.format(production*co2ReductionConst/1000)} kg/h"

            view.findViewById<Button>(R.id.popup_window_button).setOnClickListener {
                popup.dismiss()
            }

            //Shows the popup as a dropdown under card
            //popup.showAsDropDown(card)
            popup.showAtLocation(card, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER)

            return popup
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.wind_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                holder.bind(filterItems[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val search = constraint.toString()
                if (search.isEmpty()) {
                    filterItems = items
                } else {
                    val resultList: MutableList<WindParkData> = mutableListOf()
                    for (park in items) {
                        if (park.name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) or park.location.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                            resultList.add(park)
                        }
                    }
                    filterItems = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterItems
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterItems = results?.values as MutableList<WindParkData>
                notifyDataSetChanged()
            }
        }
    }
}