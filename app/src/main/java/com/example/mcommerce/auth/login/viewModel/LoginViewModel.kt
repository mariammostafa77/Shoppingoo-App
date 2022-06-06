package com.example.mcommerce.auth.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.auth.login.model.CustomerModel
import com.example.mcommerce.auth.login.model.cust_details
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(repo: RepositoryInterface) : ViewModel() {
    private val iRepo: RepositoryInterface = repo
    private val myCustomer = MutableLiveData<cust_details>()


    val customer: LiveData<cust_details> = myCustomer

    fun getCustomer() {
       viewModelScope.launch {
            val result = iRepo.getCustomers()
            withContext(Dispatchers.Main) {
                myCustomer.postValue(result)

            }
        }

    }
}
