package com.example.mcommerce.me.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.mcommerce.R

class UserAddressesFragment : Fragment() {

    lateinit var address_back_icon : ImageView
    lateinit var btnAddNewAddress : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_addresses, container, false)
        initComponent(view)
        btnAddNewAddress.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, AddNewAddressFragment())
            transaction.addToBackStack(null);
            transaction.commit()
        }
        return view
    }

    private fun initComponent(view: View){
        address_back_icon = view.findViewById(R.id.address_back_icon)
        btnAddNewAddress = view.findViewById(R.id.btnAddNewAddress)
    }


}