package com.example.mcommerce.network
import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details

import com.example.mcommerce.auth.model.CustomerDetail

import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Field
import retrofit2.http.Path

interface RemoteSourceInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String): ProductDetails
    suspend fun getVariant(id:String): Variants
    suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel

    ///// Coupons
    suspend fun getDiscountCodes() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail):Response<CustomerDetail>

    suspend fun getUserDetails(id:String): CustomerDetail

    suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail>
    suspend fun changeCustomerCurrency(id: String? , customer: CustomerDetail): Response<CustomerDetail>

    suspend fun postNewDraftOrder(order: DraftOrder):Response<DraftOrder>
    suspend fun getCustomers(): cust_details

    suspend fun getShoppingCartProducts(): DraftResponse
}
