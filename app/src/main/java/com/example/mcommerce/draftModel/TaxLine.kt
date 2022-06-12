package com.example.mcommerce.draftModel

import java.io.Serializable

data class TaxLine(
    val price: String? = null,
    val rate: Double? = null,
    val title: String? = null
) : Serializable