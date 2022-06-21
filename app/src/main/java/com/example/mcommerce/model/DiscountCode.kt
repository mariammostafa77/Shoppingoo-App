package com.example.mcommerce.model

data class DiscountCode(
    val code: String? = null,
    val created_at: String? = null,
    val id: Long? = null,
    val price_rule_id: Long?= null,
    val updated_at: String?= null,
    val usage_count: Int?= null
)