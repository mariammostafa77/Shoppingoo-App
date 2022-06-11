package com.example.mcommerce.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {

        //var base_url = "https://madalex20220.myshopify.com/admin/api/2022-04/"
        val base_url="https://9d169ad72dd7620e70f56b28ae6146d9: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@madalex20220.myshopify.com/admin/api/2022-04/"
        fun getRetrofit(): Retrofit? {
            return Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}