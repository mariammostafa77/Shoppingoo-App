package com.example.mcommerce.ProductInfo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mcommerce.R
import com.example.mcommerce.model.Product


class ProductInfoFragment : Fragment() {
lateinit var output:Product


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_product_info, container, false)
        var productName:TextView=view.findViewById(R.id.txtProductName)
        output= arguments?.getSerializable("productInfo") as Product
        productName.text=output.title
        return view
    }


}