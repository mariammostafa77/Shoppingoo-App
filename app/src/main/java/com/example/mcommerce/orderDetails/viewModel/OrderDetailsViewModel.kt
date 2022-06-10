package com.example.mcommerce.orderDetails.viewModel


import androidx.lifecycle.ViewModel
import com.example.mcommerce.model.RepositoryInterface


class OrderDetailsViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo

}
