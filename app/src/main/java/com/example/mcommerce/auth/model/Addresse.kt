package com.example.mcommerce.auth.model

import java.io.Serializable

data class Addresse(
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val company: String? = null,
    val country: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val customer_id: Long? = null,
    val default: Boolean? = null,
    val first_name: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val province: String? = null,
    val province_code: String? = null,
    val zip: String? = null
) : Serializable