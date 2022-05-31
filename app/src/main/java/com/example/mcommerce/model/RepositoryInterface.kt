package com.example.mcommerce.model

import com.example.mcommerce.home.model.BrandsModel

interface RepositoryInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts():AllProductsModel
}