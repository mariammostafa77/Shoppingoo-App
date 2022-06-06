package com.example.mcommerce.auth.model

data class CustomerX(
    val accepts_marketing: Boolean? = null,
    val accepts_marketing_updated_at: String? = null,
    var addresses: List<Addresse>? = null,
    val admin_graphql_api_id: String? = null,
    val created_at: String? = null,
    var currency: String? = null,
    val default_address: DefaultAddress? = null,
    var email: String? = null,
    var first_name: String? = null,
    val id: Long? = null,
    var last_name: String? = null,
    val last_order_id: Any? = null,
    val last_order_name: Any? = null,
    val marketing_opt_in_level: String? = null,
    val multipass_identifier: Any? = null,
    val note: String? = null,
    val orders_count: Int? = null,
    var phone: String? = null,
    val state: String? = null,
    var tags: String? = null,
    val tax_exempt: Boolean? = null,
    val tax_exemptions: List<Any>? = null,
    val total_spent: String? = null,
    val updated_at: String? = null,
    var verified_email: Boolean? = null
   // var password:String?=null,
   // var password_confirmation:String?=null
)