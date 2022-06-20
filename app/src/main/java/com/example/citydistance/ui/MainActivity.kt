package com.example.citydistance.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.citydistance.R
import com.example.citydistance.data.City
import com.example.citydistance.data.formatDistance
import com.example.citydistance.data.valueOrDefault
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var cityName:List<String> = listOf()
    private var selectedFromCityIndex : Int? = null
    private var selectedToCityIndex : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUi()
        setUpListener()

    }

    private fun getCityList():List<City> = Gson().fromJson(
        this.assets.open("cities.json").reader(),
        object: TypeToken<List<City>>(){}.type
    )

    private fun createAdapter(list: List<String>) = ArrayAdapter(
        this,
        android.R.layout.simple_list_item_1,
        list
    )

    private fun setUpUi() {
        cityName = getCityList().map {
            it.name
        }
        acTvFromCityName.setAdapter(createAdapter(cityName))
        acTvToCityName.setAdapter(createAdapter(cityName))
    }

    @SuppressLint("SetTextI18n")
    private fun setUpListener(){
        acTvFromCityName.doAfterTextChanged { searchedCityName ->
            if (searchedCityName.toString().isNotEmpty()) {
                if (cityName.contains(searchedCityName.toString())) {
                    tlFromCityName.error = null
                    selectedFromCityIndex = cityName.indexOf(searchedCityName.toString())
                }else {
                    tlFromCityName.error = "No City Found"
                }
            }
        }
        acTvToCityName.doAfterTextChanged { searchedCityName ->
            if (searchedCityName.toString().isNotEmpty()) {
                if (cityName.contains(searchedCityName.toString())) {
                    tlToCityName.error = null
                    selectedToCityIndex = cityName.indexOf(searchedCityName.toString())
                }else {
                    tlToCityName.error = "No City Found"
                }
            }
        }
        uiBtnSubmit.setOnClickListener{

            if (validateCities()){
                calculateDistance()
            }

        }
    }

    private fun calculateDistance() {
        val startPoint = Location("locationA")
        val fromLatitude = selectedFromCityIndex?.let { getCityList()[it].lat }
        val fromLongitude = selectedFromCityIndex?.let { getCityList()[it].lng }
        if (fromLatitude != null && fromLongitude != null) {
            startPoint.latitude = fromLatitude
            startPoint.longitude = fromLongitude
        }

        val endPoint = Location("locationB")
        val toLatitude = selectedToCityIndex?.let { getCityList()[it].lat }
        val toLongitude = selectedToCityIndex?.let { getCityList()[it].lng }
        if (toLongitude != null && toLatitude != null) {
            startPoint.latitude = toLatitude
            startPoint.longitude = toLongitude
        }

        val distance = ((startPoint.distanceTo(endPoint))/1000).toDouble().valueOrDefault().formatDistance()
        uiTvDistance.text = distance
    }

    private fun validateCities(): Boolean {
        if (selectedFromCityIndex == null){
            Toast.makeText(this, "Select From City", Toast.LENGTH_LONG).show()
            return false
        }else if (selectedToCityIndex == null){
            Toast.makeText(this, "Select To City", Toast.LENGTH_LONG).show()
            return false
        }else if (selectedFromCityIndex == selectedToCityIndex) {
            Toast.makeText(this, "Choose Different City", Toast.LENGTH_LONG).show()
            return false
        }else return true
    }
}







