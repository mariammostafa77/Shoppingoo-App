package com.example.mcommerce.me.view.setting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.R
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class AddNewAddressFragment : Fragment() {

    lateinit var chooseLocationImg : ImageView
    lateinit var txtAddressLine1 : EditText
    lateinit var txtAddressLine2 : EditText
    lateinit var txtPhoneNumber : EditText
    lateinit var txtCountry : TextView
    lateinit var txtCity: TextView
    lateinit var txtZipCode: TextView
    lateinit var btnSaveNewAddress : Button
    lateinit var addressDataLayout: ConstraintLayout

    lateinit var customerViewModel: CustomerViewModel
    lateinit var customerViewModelFactory: CustomerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_add_new_address, container, false)
        initComponent(view)

        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)
        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
        val customerId = sharedPreferences.getString("cusomerID",null).toString()

            chooseLocationImg.setOnClickListener {
                checkLocationPermission()
            }
        val bundle = arguments
        var userAddress = emptyList<String>()
        if (bundle != null) {
            addressDataLayout.isVisible = true
            btnSaveNewAddress.isVisible = true
            userAddress = bundle.getStringArrayList("adress")!!
            txtCountry.append(" ${userAddress?.get(0).toString()}")
            txtCity.append(" ${userAddress?.get(1).toString()}")
            txtZipCode.append(" ${userAddress?.get(3).toString()}")
        } else{
            addressDataLayout.isVisible = false
            btnSaveNewAddress.isVisible = false
        }
        btnSaveNewAddress.setOnClickListener {
            val customer = CustomerX()
            customer.addresses = listOf(Addresse(address1 =txtAddressLine1.text.toString(), address2 = txtAddressLine2.text.toString(),
                phone = txtPhoneNumber.text.toString(),city = userAddress.get(1),province = "",zip = userAddress.get(3),country = userAddress.get(0) ))
            val customDetail = CustomerDetail(customer)
            customerViewModel.addNewCustomerAddress(customerId,customDetail)
            customerViewModel.newCustomerAddress.observe(viewLifecycleOwner) { response ->
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"Updated Successfull: "+response.code().toString(),Toast.LENGTH_LONG).show()
                    Log.i("update","messs from success: "+response.body().toString())
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayout, UserAddressesFragment())
                    transaction.addToBackStack(null);
                    transaction.commit()
                }
                else{
                    Toast.makeText(requireContext(),"Updated failed: "+response.code().toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

    private fun initComponent(view: View) {
        chooseLocationImg = view.findViewById(R.id.chooseLocationImg)
        addressDataLayout = view.findViewById(R.id.addressDataLayout)
        txtAddressLine1 = view.findViewById(R.id.txtAddressLine1)
        txtAddressLine2 = view.findViewById(R.id.txtAddressLine2)
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber)
        txtCountry = view.findViewById(R.id.txtCountry)
        txtCity = view.findViewById(R.id.txtCity)
        txtZipCode = view.findViewById(R.id.txtZipCode)
        btnSaveNewAddress = view.findViewById(R.id.btnSaveNewAddress)
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
                        if (isLocationEnabled()) {
                            val intent = Intent(requireContext(), MapActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        }
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

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}