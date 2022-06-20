package com.example.mcommerce.model

import java.io.Serializable

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
): Serializable