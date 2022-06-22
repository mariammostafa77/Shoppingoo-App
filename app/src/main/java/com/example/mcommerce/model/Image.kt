package com.example.mcommerce.model

import java.io.Serializable

data class Image(
    val admin_graphql_api_id: String? = null,
    val alt: Any? = null,
    val created_at: String? = null,
    val height: Int? = null,
    val id: Long? = null,
    val position: Int? = null,
    val product_id: Long? = null,
    val src: String? = null,
    val updated_at: String? = null,
    val variant_ids: List<Any>? = null,
    val width: Int? = null
): Serializable