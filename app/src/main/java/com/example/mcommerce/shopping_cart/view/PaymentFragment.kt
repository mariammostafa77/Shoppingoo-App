package com.example.mcommerce.shopping_cart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.auth.model.Addresse

class PaymentFragment : Fragment() {

    lateinit var communicator: Communicator
    lateinit var selectedAddress: Addresse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        communicator = activity as Communicator
        if(arguments != null){
            selectedAddress = arguments?.getSerializable("userAddress") as Addresse
            Toast.makeText(requireContext(),"${selectedAddress.city}",Toast.LENGTH_SHORT).show()
        }

        return view
    }

}