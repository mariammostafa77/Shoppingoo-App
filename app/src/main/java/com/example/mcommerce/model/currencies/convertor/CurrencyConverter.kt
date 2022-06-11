package com.example.mcommerce.model.currencies.convertor

data class CurrencyConverter(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)
