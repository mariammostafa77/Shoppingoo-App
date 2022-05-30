package com.example.mcommerce.network

import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel

class BrandsClient : RemoteSourceInterface {

    companion object {
        private var instance: BrandsClient? = null
        fun getInstance(): BrandsClient {
            return instance ?: BrandsClient()
        }
    }

    override suspend fun getAllProducts(): AllProductsModel {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getProducts()
        return response!!
    }

    override suspend fun getAllBrands(): BrandsModel {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getBrands()
        return response!!
    }


}