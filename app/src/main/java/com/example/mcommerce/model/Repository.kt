package com.example.mcommerce.model

import android.content.Context
import android.util.Log
import androidx.fragment.app.strictmode.SetRetainInstanceUsageViolation
import androidx.lifecycle.LiveData
import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.network.RemoteSourceInterface
import com.example.mcommerce.network.RetrofitHelper
import com.example.mcommerce.network.ServiceApi
import com.example.mcommerce.orders.model.Orders
import retrofit2.Response
import java.util.*

class Repository private constructor(var remoteSource: RemoteSourceInterface, var context: Context)
    : RepositoryInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSourceInterface,
            context: Context
        ): Repository {
            return instance ?: Repository(
                remoteSource,context
            )
        }
    }
    override suspend fun getAllProducts(): AllProductsModel {
        return remoteSource.getAllProducts()
    }

    override suspend fun getAllBrands(): BrandsModel {
        return remoteSource.getAllBrands()
    }

    override suspend fun getBrandProducts(id:String): AllProductsModel {
        return remoteSource.getBrandProducts(id)
    }

    override suspend fun getSpecificProduct(id:String): ProductDetails {

        return remoteSource.getSpecificProduct(id)
    }

    override suspend fun getVariant(id: String):Variants{
        return remoteSource.getVariant(id)
    }

    override suspend fun getDiscountsCods(): DiscountCodesModel {
        return  remoteSource.getDiscountCodes()
    }

    override suspend fun postNewCustomer(customer: CustomerDetail): Response<CustomerDetail> {
        return  remoteSource.postNewCustomer(customer)
    }
    override suspend fun getSubCategories(vendor: String,productType:String,collectionId:String):AllProductsModel{
        return  remoteSource.getSubCategories(vendor,productType,collectionId)
    }

    override suspend fun getProductTypes(id: String):AllProductsModel {
        return remoteSource.getProductTypes(id)
    }

    override suspend fun getCustomerDetails(id: String): CustomerDetail {
        return remoteSource.getUserDetails(id)
    }

    override suspend fun addNewAddress(id: String?, customer: CustomerDetail): Response<CustomerDetail> {
        return remoteSource.addNewAddress(id,customer)
    }

    override suspend fun changeCustomerCurrency(id: String? , currency: String): Response<CustomerDetail> {
        return remoteSource.changeCustomerCurrency(id,currency)
    }

    override suspend fun postNewDraftOrder(order: DraftOrder): Response<DraftOrder> {
        return  remoteSource.postNewDraftOrder(order)
    }

    override suspend fun getCustomers(): cust_details {
        return  remoteSource.getCustomers()
    }

    override suspend fun deleteProductFromShoppingCart(id: String?): Response<DraftOrder> {
        return remoteSource.deleteProductFromShoppingCart(id)
    }

    override suspend fun updateDraftOrder(id: String?, order: DraftOrder): Response<DraftOrder> {
        return remoteSource.updateDraftOrder(id,order)
    }

    override suspend fun getOrders(id: String): Orders {
        return remoteSource.getOrders(id)
    }

    override suspend fun getShoppingCartProducts(): DraftResponse {
        return remoteSource.getShoppingCartProducts()
    }

}