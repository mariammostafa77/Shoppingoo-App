package com.example.mcommerce.me.view.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.mcommerce.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.ArrayList

class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var predictionList: List<AutocompletePrediction>

    private lateinit var mLastKnownLocation: Location
    private lateinit var locationCallback: LocationCallback

    private lateinit var map_search_bar : SearchView
    private lateinit var mapView: View

    private val DEFAULT_ZOOM = 15f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map_search_bar = findViewById(R.id.map_search_bar)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mapView = mapFragment.view!!
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(this, getString(R.string.map_key))
        placesClient = Places.createClient(this)
        val token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

        map_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(newText)
                    .build()
                placesClient!!.findAutocompletePredictions(predictionsRequest)
                    .addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val predictionsResponse = task.result
                                if (predictionsResponse != null) {
                                    predictionList = predictionsResponse.autocompletePredictions
                                    val suggestionsList: MutableList<String?> = ArrayList()
                                    for (i in predictionList!!.indices) {
                                        val prediction = predictionList!![i]
                                        suggestionsList.add(prediction.getFullText(null).toString())
                                    }
                                    /*
                                    map_search_bar.updateLastSuggestions(suggestionsList)
                                    if (!map_search_bar.isSuggestionsVisible()) {
                                        map_search_bar.showSuggestionsList()
                                    }
                                    */
                                }
                            } else {
                                Log.i("mytag", "prediction fetching task unsuccessful")
                            }
                        })

                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        if (mapView != null && mapView.findViewById<View?>("1".toInt()) != null) {
            val locationButton =
                (mapView.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 40, 180)
        }
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener(this) {
            getDeviceLocation()
        }

        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                val resolvable = e
                try {
                    resolvable.startResolutionForResult(this, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mLastKnownLocation = task.result
                    if (mLastKnownLocation != null) {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(mLastKnownLocation.latitude, mLastKnownLocation.longitude)
                                , DEFAULT_ZOOM)
                        )
                    } else {
                        val locationRequest = LocationRequest.create()
                        locationRequest.interval = 10000
                        locationRequest.fastestInterval = 5000
                        locationRequest.priority =
                            LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                              /*  if (locationResult == null) {
                                    return
                                }
                               */
                                mLastKnownLocation = locationResult.lastLocation
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        LatLng(mLastKnownLocation.latitude, mLastKnownLocation.longitude)
                                    , DEFAULT_ZOOM) )
                                mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
                            }
                        }
                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                            Looper.myLooper()!!)
                    }
                } else {
                    Toast.makeText(this, "unable to get last location", Toast.LENGTH_SHORT).show()
                }
            }
    }



}