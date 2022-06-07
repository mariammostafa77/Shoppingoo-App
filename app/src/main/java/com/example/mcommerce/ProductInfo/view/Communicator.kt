package com.example.mcommerce.ProductInfo.view

import com.example.mcommerce.model.Product

interface Communicator {
    fun passProductData(product:Product)
    fun goFromBrandToCategories(brandName:String)
    fun goToSearchWithID(id:String)
    fun goToSearchWithAllData(id:String,brandName:String,subCatName:String)
}