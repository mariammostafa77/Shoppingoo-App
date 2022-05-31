package com.example.mcommerce.ProductInfo.view

import com.example.mcommerce.model.Product

interface Communicator {
    fun passProductData(product:Product)
}