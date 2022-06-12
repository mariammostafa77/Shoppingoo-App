package com.example.mcommerce.model


import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.currencies.CurrencyResponse
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import com.example.mcommerce.network.RetrofitHelper
import com.example.mcommerce.network.ServiceApi
import com.example.mcommerce.orders.model.OrderResponse
import com.example.mcommerce.orders.model.Orders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path
import java.util.*

interface RepositoryInterface {
    suspend fun getAllProducts():AllProductsModel
    suspend fun getAllBrands():BrandsModel
    suspend fun getBrandProducts(id:String):AllProductsModel
    suspend fun getSpecificProduct(id:String):ProductDetails
    suspend fun getVariant(id:String): Variants
    suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel

    suspend fun getProductTypes(id : String):AllProductsModel

    suspend fun getDiscountsCods() : DiscountCodesModel
    suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail>

    suspend fun getCustomerDetails(id:String):CustomerDetail
    suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail>

    suspend fun changeCustomerCurrency(id: String? , currency: String): Response<CustomerDetail>

    suspend fun postNewDraftOrder(order: DraftOrder):Response<DraftOrder>

    suspend fun getShoppingCartProducts(): DraftResponse

    suspend fun getCustomers(): Customer

    suspend fun deleteProductFromShoppingCart(id: String?): Response<DraftOrder>

    suspend fun updateDraftOrder(id: String? , order: DraftOrder):Response<DraftOrder>
    suspend fun getOrders(id : String): Orders
    suspend fun getAllCurrencies(): CurrencyResponse

    suspend fun getCurrencyValue(to: String): CurrencyConverter

    suspend fun postNewOrder(orders: OrderResponse): Response<OrderResponse>

}