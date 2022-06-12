package com.example.mcommerce.draftModel

import java.io.Serializable

data class OrderPrices(
    var tax: Double = 0.0,
    var subTotal: Double = 0.0,
    var total: Double = 0.0
): Serializable
