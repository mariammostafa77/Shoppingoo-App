package com.example.mcommerce.shopping_cart.view

import android.widget.TextView
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX

interface OnShoppingCartClickListener {
    fun onDeleteItemClickListener(draftOrder: DraftOrder)
    fun onIncrementClickListener(draftOrder: DraftOrder, position : Int)
    fun onDecrementClickListener(draftOrder: DraftOrder, position : Int)



}