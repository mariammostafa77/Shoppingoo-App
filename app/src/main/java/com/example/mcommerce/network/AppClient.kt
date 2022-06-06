package com.example.mcommerce.network

import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import retrofit2.Response
import retrofit2.http.Path

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

    override suspend fun getSpecificProduct(id: String): ProductDetails {
            val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
            val response = service?.getSpecificProduct(id)
            return response!!
        }

    override suspend fun getVariant(id: String): Variants {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getVariant(id)
        return response!!
    }

    override suspend fun getDiscountCodes(): DiscountCodesModel {
            val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
            val response = service?.getDiscountCodesFromNetwork()
            return response!!
        }

    override suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.postNewCustomer(customer)
        return response!!
    }

    override suspend fun postNewDraftOrder(order: DraftOrder): Response<DraftOrder> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.postNewDraftOrder(order)
        return response!!
    }

    override suspend fun getCustomers(): cust_details {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getCustomers()
        return response!!
    }
    override suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel{
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getSubCategories(vendor,productType,collectionId)
        return response!!
    }

}
