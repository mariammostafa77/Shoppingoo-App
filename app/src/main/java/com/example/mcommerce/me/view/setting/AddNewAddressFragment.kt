package com.example.mcommerce.me.view.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.mcommerce.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class AddNewAddressFragment : Fragment() {

    lateinit var chooseLocationImg : ImageView
    lateinit var countryText: TextView
    lateinit var cityText: TextView
    lateinit var areaText: TextView
    lateinit var zipCodeText: TextView
    lateinit var phoneText : TextView
    lateinit var mapDataLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_add_new_address, container, false)
        initComponent(view)
        chooseLocationImg.setOnClickListener {
            checkLocationPermission()
        }
        return view
    }

    private fun initComponent(view: View) {
        chooseLocationImg = view.findViewById(R.id.chooseLocationImg)
        countryText = view.findViewById(R.id.countryText)
        cityText = view.findViewById(R.id.cityText)
        areaText = view.findViewById(R.id.areaText)
        // zipCodeText = view.findViewById(R.id.zipCodeText)
        phoneText = view.findViewById(R.id.phoneText)
    }
    private fun checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  ){
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
        else{
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val intent = Intent(requireContext(), MapActivity::class.java)
                        startActivity(intent)
                    }
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Permission Denied")
                                .setMessage("Permission to access device location is permanently denied.\n you need to go to setting to allow the permission.")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("OK") { dialog, which ->
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    intent.data = Uri.fromParts("package", "com.example.mcommerce", null)
                                }
                                .show()
                        } else {
                            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .check()
        }

    }


}