package com.example.mcommerce.auth.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(repo: RepositoryInterface) : ViewModel() {
    private val iRepo: RepositoryInterface = repo
    private val myCustomer = MutableLiveData<Customer>()


    val customer: LiveData<Customer> = myCustomer

    fun getCustomer() {
       viewModelScope.launch {
            val result = iRepo.getCustomers()
            withContext(Dispatchers.Main) {
                myCustomer.postValue(result)

            }
        }

    }
}
