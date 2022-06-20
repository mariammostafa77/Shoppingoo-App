package com.example.mcommerce.network


import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import com.example.mcommerce.orders.model.Orders
import com.example.mcommerce.model.currencies.CurrencyResponse
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import com.example.mcommerce.orders.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Path
import java.util.*

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

    override suspend fun getCustomers(): Customer {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getCustomers()
        return response!!
    }

    override suspend fun getProductTypes(id: String): AllProductsModel {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getProductTypes(id)
        return response!!
    }

    override suspend fun getOrders(id: String): Orders {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getOrders(id)
        return response!!
    }

    override suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel{
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getSubCategories(vendor,productType,collectionId)
        return response!!
    }
    override suspend fun getShoppingCartProducts(): DraftResponse {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getShoppingCartProducts()
        return response!!
    }

    override suspend fun deleteProductFromShoppingCart(id: String?): Response<DraftOrder> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.deleteProductFromShoppingCart(id)
        return response!!
    }

    override suspend fun updateDraftOrder(id: String?, order: DraftOrder): Response<DraftOrder> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.updateDraftOrder(id,order)
        return response!!
    }

    override suspend fun getAllCurrencies(): CurrencyResponse {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getAllCurrencies()
        return response!!
    }

    override suspend fun getCurrencyValue(to: String): CurrencyConverter {
        val service = CurrencyConverterHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getCurrencyValue(to)
        return response!!
    }

    override suspend fun getCurrencyValueInEgp(from: String): CurrencyConverter {
        val service = CurrencyConverterHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getCurrencyValuesInEgp(from)
        return response!!
    }

    override suspend fun postNewOrder(orders: OrderResponse): Response<OrderResponse> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.postNewOrder(orders)
        return response!!
    }

    override suspend fun getUserDetails(id: String): CustomerDetail {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.getUserInfo(id)
        return response!!
    }

    override suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.addNewCustomerAddress(id,customer)
        return response!!
    }

    override suspend fun changeCustomerCurrency(id: String? , currency: String): Response<CustomerDetail> {
        val service = RetrofitHelper.getRetrofit()?.create(ServiceApi::class.java)
        val response = service?.changeCustomerCurrency(id,currency)
        return response!!
    }

}
