package com.example.mcommerce.orderDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class OrderDetailsViewModelFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)){
            OrderDetailsViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}