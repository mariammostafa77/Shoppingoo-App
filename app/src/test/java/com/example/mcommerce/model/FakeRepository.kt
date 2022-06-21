package com.example.mcommerce.model

import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.currencies.CurrencyResponse
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import com.example.mcommerce.network.RemoteSourceInterface
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.model.OrderResponse
import com.example.mcommerce.orders.model.Orders
import com.google.android.gms.common.util.CollectionUtils.listOf
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
//        var product=Product()
//        return myProdct
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
        var customer = CustomerX()
        customer.first_name = "Zeinab"
        customer.last_name = "Ibrahim"
        customer.email = "zeinabibrahim541@gmail.com"
        customer.verified_email = true
        var phoneNumber: String = "01143232215"
        customer.phone = phoneNumber

        //  customer.phone="01009843245"
        customer.tags = "123456789"
            customer.addresses = listOf(Addresse(address1 = "Alkafal",
                phone = "01203574583",
                city = "Alex",
                province = "",
                zip = "21552",
                last_name = "Lastnameson",
                first_name = "Mother",
                country = "EG"))
        var myCustomer=Customer(listOf(customer))
        return myCustomer
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