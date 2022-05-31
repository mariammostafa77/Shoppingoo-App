package com.example.mcommerce.network
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel

interface RemoteSourceInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts():AllProductsModel
}
