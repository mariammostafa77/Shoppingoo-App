package com.example.mcommerce.orders.model


data class ShippingAddress(
    val address1: String? = null,
    val address2: Any?= null,
    val city: String?= null,
    val company: Any?= null,
    val country: String?= null,
    val country_code: String?=null,
    val first_name: String?=null,
    val last_name: String?= null,
    val latitude: Any?=null,
    val longitude: Any?= null,
    val name: String?= null,
    val phone: String?= null,
    val province: String?= null,
    val province_code: Any?= null,
    val zip: String?= null
)