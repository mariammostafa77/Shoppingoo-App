package com.example.mcommerce.draftModel

data class Customer(
    val accepts_marketing: Boolean? = null,
    val accepts_marketing_updated_at: String? = null,
    val admin_graphql_api_id: String? = null,
    val created_at: String? = null,
    val currency: String? = null,
    val default_address: DefaultAddress? = null,
    val email: String? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val last_order_id: Any? = null,
    val last_order_name: Any? = null,
    val marketing_opt_in_level: Any? = null,
    val multipass_identifier: Any? = null,
    val note: Any? = null,
    val orders_count: Int? = null,
    val phone: String? = null,
    val sms_marketing_consent: SmsMarketingConsent? = null,
    val state: String? = null,
    val tags: String? = null,
    val tax_exempt: Boolean? = null,
    val tax_exemptions: List<Any>? = null,
    val total_spent: String? = null,
    val updated_at: String? = null,
    val verified_email: Boolean? = null
)