package com.example.mcommerce.model

import java.io.Serializable

data class Variant(
    val admin_graphql_api_id: String,
    val barcode: Any,
    val compare_at_price: Any,
    val created_at: String,
    val fulfillment_service: String,
    val grams: Int,
    val id: Long,
    val image_id: Any,
    val inventory_item_id: Long,
    val inventory_management: String,
    val inventory_policy: String,
    val inventory_quantity: Int,
    val old_inventory_quantity: Int,
    val option1: String,
    val option2: String,
    val option3: Any,
    val position: Int,
    val price: String,
    val product_id: Long,
    val requires_shipping: Boolean,
    val sku: String,
    val taxable: Boolean,
    val title: String,
    val updated_at: String,
    val weight: Double,
    val weight_unit: String
): Serializable