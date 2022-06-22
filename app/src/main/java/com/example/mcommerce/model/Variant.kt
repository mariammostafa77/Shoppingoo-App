package com.example.mcommerce.model

import java.io.Serializable

data class Variant(
    val admin_graphql_api_id: String? = null,
    val barcode: Any? = null,
    val compare_at_price: Any? = null,
    val created_at: String? = null,
    val fulfillment_service: String? = null,
    val grams: Int? = null,
    val id: Long? = null,
    val image_id: Any? = null,
    val inventory_item_id: Long? = null,
    val inventory_management: String? = null,
    val inventory_policy: String? = null,
    val inventory_quantity: Int? = null,
    val old_inventory_quantity: Int? = null,
    val option1: String? = null,
    val option2: String? = null,
    val option3: Any? = null,
    val position: Int? = null,
    val price: String? = null,
    val product_id: Long? = null,
    val requires_shipping: Boolean? = null,
    val sku: String? = null,
    val taxable: Boolean? = null,
    val title: String? = null,
    val updated_at: String? = null,
    val weight: Double? = null,
    val weight_unit: String? = null
): Serializable