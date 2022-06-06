package com.example.mcommerce.me.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CustomerViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val customerDetail = MutableLiveData<CustomerX>()
    private val updatedCustomerAddress = MutableLiveData<Response<CustomerDetail>>()
    private val customerCurrency = MutableLiveData<Response<CustomerDetail>>()

    val customerInfo: LiveData<CustomerX> = customerDetail
    val newCustomerAddress: LiveData<Response<CustomerDetail>> = updatedCustomerAddress
    val selectedCustomerCurrency : LiveData<Response<CustomerDetail>> = customerCurrency

    fun getUserDetails(id: String) {
        viewModelScope.launch {
            val result = iRepo.getCustomerDetails(id)
            withContext(Dispatchers.Main) {
                customerDetail.postValue(result.customer!!)
            }
        }
    }

    fun addNewCustomerAddress(id: String, customer: CustomerDetail){
        viewModelScope.launch{
            val result = iRepo.addNewAddress(id,customer)
            withContext(Dispatchers.Main){
                updatedCustomerAddress.value=result
            }
        }
    }

    fun changeCustomerCurrency(id: String, customer: CustomerDetail){
        viewModelScope.launch{
            val result = iRepo.changeCustomerCurrency(id,customer)
            withContext(Dispatchers.Main){
                customerCurrency.value=result
            }
        }
    }


}