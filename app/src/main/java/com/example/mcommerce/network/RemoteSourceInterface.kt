package com.example.mcommerce.network
import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details

import com.example.mcommerce.auth.model.CustomerDetail

import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import com.example.mcommerce.model.AllProductsModel
import com.example.mcommerce.model.ProductDetails
import com.example.mcommerce.model.DiscountCodesModel
import com.example.mcommerce.model.*
import com.example.mcommerce.orders.model.Orders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Field
import java.util.*


interface RemoteSourceInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String): ProductDetails
    suspend fun getVariant(id:String): Variants
    suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel

    suspend fun getShoppingCartProducts(): DraftResponse

    ///// Coupons
    suspend fun getDiscountCodes() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail):Response<CustomerDetail>

    suspend fun getUserDetails(id:String): CustomerDetail

    suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail>
    suspend fun changeCustomerCurrency(id: String? , currency: String): Response<CustomerDetail>

    suspend fun postNewDraftOrder(order: DraftOrder):Response<DraftOrder>
    suspend fun getCustomers(): cust_details

    suspend fun getProductTypes(id : String): AllProductsModel
    suspend fun getOrders(id : String): Orders

    suspend fun deleteProductFromShoppingCart(id: String?): Response<DraftOrder>

    suspend fun updateDraftOrder(id: String? , order: DraftOrder):Response<DraftOrder>

}
