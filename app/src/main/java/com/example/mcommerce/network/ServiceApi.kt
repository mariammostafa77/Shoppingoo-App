  package com.example.mcommerce.network

import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import com.example.mcommerce.model.currencies.CurrencyResponse
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import retrofit2.Response
import retrofit2.http.*
import java.util.*

  interface ServiceApi {
    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("products.json")
    suspend fun getProducts(): AllProductsModel
    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("smart_collections.json")
    suspend fun getBrands(): BrandsModel
    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("collections/"+"{id}"+"/products.json")
    suspend fun getBrandProducts(@Path("id") id: String?): AllProductsModel
      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",
      )
      @GET("products/"+"{id}"+".json")
      suspend fun getSpecificProduct(@Path("id") id: String?): ProductDetails
      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",
      )
      @GET("products/"+"{id}"+"/variants.json")
      suspend fun getVariant(@Path("id") id: String?): Variants
      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",
      )
      @GET("products.json")
      suspend fun getSubCategories(@Query("vendor") vendor: String?,
                                   @Query("product_type") productType: String?,
                                    @Query("collection_id") collectionId: String?): AllProductsModel

    //// https://madalex20220.myshopify.com/admin/api/2022-04/price_rules/1089622311051/discount_codes.json
      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",
      )
      @GET("price_rules/1089622311051/discount_codes.json")
      suspend fun getDiscountCodesFromNetwork(): DiscountCodesModel

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @POST("customers.json")
      suspend fun postNewCustomer(@Body customer: CustomerDetail):Response<CustomerDetail>
      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @POST("draft_orders.json")
      suspend fun postNewDraftOrder(@Body order: DraftOrder):Response<DraftOrder>

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @GET("customers.json")
      suspend fun getCustomers(): Customer

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @PUT("customers/"+"{id}"+".json")
      suspend fun addNewCustomerAddress(@Path("id") id: String? , @Body customer: CustomerDetail): Response<CustomerDetail>

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @GET("customers/"+"{id}"+".json")
      suspend fun getUserInfo(@Path("id")id: String?): CustomerDetail

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @FormUrlEncoded
      @PATCH("customers/"+"{id}"+".json")
      suspend fun changeCustomerCurrency(@Path("id") id: String? ,@Field("currency") currency: String): Response<CustomerDetail>

      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",)
      @GET("draft_orders.json")
      suspend fun getShoppingCartProducts(): DraftResponse

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @DELETE("draft_orders/"+"{draft_order_id}"+".json")
      suspend fun deleteProductFromShoppingCart(@Path("draft_order_id") id: String?): Response<DraftOrder>

      @Headers(
          "X-Shopify-Shop-Api-Call-Limit: 40/40",
          "Retry-After: 2.0",
          "Accept: application/json",
          "X-Shopify-Access-Token: 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"
      )
      @PUT("draft_orders/{id}.json")
      suspend fun updateDraftOrder(@Path("id") id: String? , @Body order: DraftOrder):Response<DraftOrder>

      // https://9d169ad72dd7620e70f56b28ae6146d9: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@madalex20220.myshopify.com/admin/api/2022-04/currencies.json

      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",)
      @GET("currencies.json")
      suspend fun getAllCurrencies(): CurrencyResponse

      // https://api.apilayer.com/exchangerates_data/convert?to=EGP&from=USD&amount=1&apikey=OdsWOfPbLEyojdjFR7FjcSzVpifcX23n
      @GET("convert?apikey=bvWIQqwc5PjLwYrSgElp83ZEktkQWLJB&amount=1&from=EGP")
      suspend fun getCurrencyValue(@Query("to") to: String): CurrencyConverter

      @Headers(
          "Accept: application/json",
          "X-Shopify-Access-Token: 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",
      )
      @GET("collections/"+"{id}"+"/products.json?fields=product_type")
      suspend fun getProductTypes(@Path("id") id:String?): AllProductsModel



}

