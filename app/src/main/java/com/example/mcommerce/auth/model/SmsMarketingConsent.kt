package com.example.mcommerce.auth.model

data class SmsMarketingConsent(
    val consent_collected_from: String,
    val consent_updated_at: String,
    val opt_in_level: String,
    val state: String
)