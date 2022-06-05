package com.example.mcommerce.model

import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.home.model.BrandsModel
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String):ProductDetails

    suspend fun getDiscountsCods() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail>
}