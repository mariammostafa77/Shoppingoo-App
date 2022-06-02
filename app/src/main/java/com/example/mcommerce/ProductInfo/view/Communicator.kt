package com.example.mcommerce.ProductInfo.view

import com.example.mcommerce.model.Product

interface Communicator {
    fun passProductData(product:Product)
    fun goToProductInfo(id:String,brandName:String)
    fun goToSearchWithID(id:String)
}