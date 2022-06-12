package com.example.mcommerce.orders.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class OrdersViewFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrdersViewModel::class.java)){
            OrdersViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}