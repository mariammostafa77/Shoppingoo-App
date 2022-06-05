package com.example.mcommerce.auth.Register.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.DiscountCode
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegisterViewModel (repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val newCustomer = MutableLiveData<Response<CustomerDetail>>()


    init {


    }

    val customer: LiveData<Response<CustomerDetail>> = newCustomer

    fun postCustomer(customerX: CustomerDetail){
        viewModelScope.launch{
            val result = iRepo.postNewCustomer(customerX)
            withContext(Dispatchers.Main){

                newCustomer.value=result

            }
        }

    }









}
