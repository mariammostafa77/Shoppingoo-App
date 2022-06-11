package com.example.mcommerce.model.currencies

data class CurrencyModel(
    val currency: String,
    val enabled: Boolean,
    val rate_updated_at: String
)
