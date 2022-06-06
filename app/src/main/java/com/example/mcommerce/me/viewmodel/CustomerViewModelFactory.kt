package com.example.mcommerce.me.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class CustomerViewModelFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            CustomerViewModel(repo) as T
        } else {
            throw IllegalArgumentException("This Class not found")
        }
    }
}