package com.example.mcommerce.model

import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.currencies.CurrencyResponse
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import com.example.mcommerce.network.RemoteSourceInterface
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.model.OrderResponse
import com.example.mcommerce.orders.model.Orders
import retrofit2.Response

class FakeRepository:RemoteSourceInterface {
    var ordersList:MutableList<Order> = mutableListOf()
    lateinit var ordersObj:Orders

    fun setOrders(orders:MutableList<Order>){
        this.ordersList=orders
        ordersObj= Orders(ordersList)
    }

    override suspend fun getAllProducts(): AllProductsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBrands(): BrandsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getBrandProducts(id: String): AllProductsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getSpecificProduct(id: String): ProductDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getVariant(id: String): Variants {
        TODO("Not yet implemented")
    }

    override suspend fun getSubCategories(
        vendor: String,
        productType: String,
        collectionId: String
    ): AllProductsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscountCodes(): DiscountCodesModel {
        TODO("Not yet implemented")
    }

    override suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(id: String): CustomerDetail {
        TODO("Not yet implemented")
    }

    override suspend fun addNewAddress(
        id: String?,
        customer: CustomerDetail
    ): Response<CustomerDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun changeCustomerCurrency(
        id: String?,
        currency: String
    ): Response<CustomerDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun postNewDraftOrder(order: DraftOrder): Response<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductTypes(id: String): AllProductsModel {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(id: String): Orders {
        val ordersList= mutableListOf<Order>(
            Order(contact_email="mariammostafa@gmail.com"),
            Order(contact_email="mariammostafa@gmail.com")
        )
        var ordersObj=Orders(ordersList)
        return ordersObj
    }

    override suspend fun getCustomers(): Customer {
        TODO("Not yet implemented")
    }

    override suspend fun getShoppingCartProducts(): DraftResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductFromShoppingCart(id: String?): Response<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(id: String?, order: DraftOrder): Response<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCurrencies(): CurrencyResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencyValue(to: String): CurrencyConverter {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencyValueInEgp(from: String): CurrencyConverter {
        TODO("Not yet implemented")
    }

    override suspend fun postNewOrder(orders: OrderResponse): Response<OrderResponse> {
        TODO("Not yet implemented")
    }
}