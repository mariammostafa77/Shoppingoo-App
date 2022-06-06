  package com.example.mcommerce.network
import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.model.*
import retrofit2.Response
import retrofit2.http.*

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
      suspend fun getCustomers(): cust_details

}

