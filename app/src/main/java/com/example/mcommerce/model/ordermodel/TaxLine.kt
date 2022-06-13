package com.example.mcommerce.model.ordermodel

data class TaxLine(
    val channel_liable: Any,
    val price: String,
    val price_set: PriceSetX,
    val rate: Double,
    val title: String
)