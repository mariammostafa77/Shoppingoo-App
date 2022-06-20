package com.example.mcommerce.me.view.setting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MapActivity : AppCompatActivity() , OnMapReadyCallback {

  private lateinit var mMap: GoogleMap
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation : Location?= null
    var currentMarker: Marker? = null
    lateinit var lastLocation : Location

    private lateinit var mapSearchView : EditText
    private lateinit var imgSearchBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapSearchView = findViewById(R.id.map_search_bar)
        imgSearchBtn = findViewById(R.id.imgSearchBtn)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        /*
        mapSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
               // searchLocation()
            }
            override fun afterTextChanged(editable: Editable) {
                searchLocation()
            }
        })
        */
        imgSearchBtn.setOnClickListener {
            mapSearchView.onEditorAction(EditorInfo.IME_ACTION_DONE)
            searchInLocation(it)
        }
    }

    fun searchInLocation(view : View) {
        lateinit var searchLocation: String
        searchLocation = mapSearchView.text.toString()
        var addressList: List<Address>? = null

        if (searchLocation.isNullOrEmpty()) {
            val snakbar: Snackbar = Snackbar.make(view,"Type Any Location",Snackbar.LENGTH_LONG)
            snakbar.show()
        }
        else{
            val searchGeoCoder = Geocoder(this)
            try {
                addressList = searchGeoCoder.getFromLocationName(searchLocation, 1)
                if(addressList.size>0){
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f))
                }
                else{
                    val snakbar: Snackbar = Snackbar.make(view,"Not Found!!",Snackbar.LENGTH_LONG)
                    snakbar.show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
            val latLng = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f))
            mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
                override fun onMapClick(p0: LatLng) {
                    if(currentMarker != null){
                        currentMarker?.remove()
                    }
                    val newLatLng = LatLng(p0.latitude,p0.longitude)
                    drawMarker(newLatLng)
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener {location ->
            if(location != null){
                this.currentLocation = location
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            }
        }
    }

    private fun drawMarker(latLng: LatLng){
        val address = getAddress(latLng.latitude, latLng.longitude)
        val markerOptions=  MarkerOptions().position(latLng).title("Your Location")
                .snippet(address?.get(0)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        currentMarker=  mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
        if (address != null) {
            val intent = Intent(this, HomeActivity()::class.java)
            intent.putStringArrayListExtra("userAddress",address)
            startActivity(intent)
        }
    }

    private fun getAddress(lat:Double, lon: Double): ArrayList<String>?{
        val geocoder= Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat,lon,1)
        val countryName = address[0].countryName
        val cityName =  address[0].adminArea
        val countryCode = address[0].countryCode
        val zipCode = address[0].postalCode
        val userAdress : ArrayList<String> = ArrayList()
        userAdress.add(countryName)
        userAdress.add(cityName)
        userAdress.add(countryCode)
        userAdress.add(zipCode)
        userAdress.add(lat.toString())
        userAdress.add(lon.toString())
        return userAdress
    }

}