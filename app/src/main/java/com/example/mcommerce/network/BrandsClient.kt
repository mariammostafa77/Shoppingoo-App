package com.example.mcommerce.network

import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel

class BrandsClient private constructor(var id:String) : RemoteSourceInterface {

    companion object {
        private var instance: BrandsClient? = null
        fun getInstance(id:String): BrandsClient {
            return instance ?: BrandsClient(id)
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

    override suspend fun getBrandProducts(): AllProductsModel {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getBrandProducts(id)
        return response!!
    }


}