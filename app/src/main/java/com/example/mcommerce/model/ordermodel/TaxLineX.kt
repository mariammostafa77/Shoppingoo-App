package com.example.mcommerce.model.ordermodel

data class TaxLineX(
    val channel_liable: Any,
    val price: String,
    val price_set: PriceSetXX,
    val rate: Double,
    val title: String
)