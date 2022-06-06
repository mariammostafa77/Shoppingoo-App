package com.example.mcommerce.model

import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Path

interface RepositoryInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String):ProductDetails

    suspend fun getDiscountsCods() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail>

    suspend fun getCustomerDetails(id:String):CustomerDetail
    suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail>

    suspend fun changeCustomerCurrency(id: String? , customer: CustomerDetail): Response<CustomerDetail>

    suspend fun postNewDraftOrder(order: DraftOrder):Response<DraftOrder>

    suspend fun getShoppingCartProducts(): DraftResponse

}