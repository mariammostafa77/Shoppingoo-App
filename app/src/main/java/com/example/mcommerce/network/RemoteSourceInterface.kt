package com.example.mcommerce.network
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.ProductDetails

interface RemoteSourceInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String): ProductDetails
}
