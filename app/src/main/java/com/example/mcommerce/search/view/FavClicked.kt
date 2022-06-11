package com.example.mcommerce.search.view

import android.widget.ImageView
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.model.Product

interface FavClicked {
    fun addToFav(product:Product,img:ImageView,myIndex:Int)
    fun addFavImg(img:ImageView,id: Long)
}