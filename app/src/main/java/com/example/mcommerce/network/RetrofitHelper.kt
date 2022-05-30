package com.example.mcommerce.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        var base_url = "https://madalex20220.myshopify.com/admin/api/2022-04/"
        fun getRetrofit(): Retrofit? {
            return Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}