package com.example.mcommerce.shopping_cart.view

import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX

interface OnShoppingCartClickListener {
    fun onDeleteItemClickListener(draftOrder: DraftOrder)
    fun onIncrementClickListener(draftOrder: DraftOrder)
    fun onDecrementClickListener(draftOrder: DraftOrder)


}