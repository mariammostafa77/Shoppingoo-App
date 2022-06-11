package com.example.mcommerce.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyConverterHelper {

    companion object {

        var currency_base_url = "https://api.apilayer.com/exchangerates_data/"
        fun getRetrofit(): Retrofit? {
            return Retrofit.Builder().baseUrl(currency_base_url)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }

}