package com.example.mcommerce.model

import java.io.Serializable

data class Option(
    val id: Long? = null,
    val name: String? = null,
    val position: Int? = null,
    val product_id: Long? = null,
    val values: List<String>? = null
): Serializable