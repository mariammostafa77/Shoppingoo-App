package com.example.mcommerce.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.model.Repository
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import java.lang.IllegalArgumentException

class FavViewModelFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)){
            FavViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}