package com.example.mcommerce.network

import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel

class AppClient : RemoteSourceInterface {

    companion object {
        private var instance: AppClient? = null
        fun getInstance(): AppClient {
            return instance ?: AppClient()
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

    override suspend fun getBrandProducts(id:String): AllProductsModel {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getBrandProducts(id)
        return response!!
    }


}