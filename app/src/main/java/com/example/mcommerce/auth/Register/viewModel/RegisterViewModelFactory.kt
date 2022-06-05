package com.example.mcommerce.auth.Register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.model.Repository
import java.lang.IllegalArgumentException

class RegisterViewModelFactory(val repo: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            RegisterViewModel(repo) as T
        }
        else{
            throw IllegalArgumentException("This Class not found")
        }
    }
}