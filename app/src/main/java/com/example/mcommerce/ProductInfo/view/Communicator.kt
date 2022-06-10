package com.example.mcommerce.ProductInfo.view

import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.model.Product
import com.example.mcommerce.orders.model.Order

interface Communicator {
    fun passProductData(product:Product)
    fun goFromBrandToCategories(brandName:String)
    fun goToSearchWithID(id:String)
    fun goToSearchWithAllData(id:String,brandName:String,subCatName:String)

    fun goToUserAddresses()
    fun goToPaymentFromAddress(selectedAddress: Addresse)
    fun goToOrderDetails(selectedOrder: Order)

}