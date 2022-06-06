package com.example.mcommerce.shopping_cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class ShoppingCartViewModelFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)){
            ShoppingCartViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}