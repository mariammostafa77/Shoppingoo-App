package com.example.mcommerce.network
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.AllProductsModel
import com.example.mcommerce.model.ProductDetails
import com.example.mcommerce.model.DiscountCodesModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path

interface RemoteSourceInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String): ProductDetails

    ///// Coupons
    suspend fun getDiscountCodes() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail):Response<CustomerDetail>

    suspend fun getUserDetails(id:String): CustomerDetail

    suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail>
    suspend fun changeCustomerCurrency(id: String? , customer: CustomerDetail): Response<CustomerDetail>

}
