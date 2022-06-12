package com.example.mcommerce.categories.view

import android.widget.ImageView
import com.example.mcommerce.model.Product

interface CurrencyConvertor {

    fun onPriceConverter(position: Int) : String
    fun addToFav(product: Product, img: ImageView, myIndex:Int)
    fun addFavImg(img: ImageView, id: Long)

}