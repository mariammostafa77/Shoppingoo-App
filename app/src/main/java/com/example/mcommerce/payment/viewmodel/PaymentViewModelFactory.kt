package com.example.mcommerce.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class PaymentViewModelFactory (val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PaymentViewModel::class.java)){
            PaymentViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}