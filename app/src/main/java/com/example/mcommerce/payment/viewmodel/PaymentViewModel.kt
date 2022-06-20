package com.example.mcommerce.payment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.RepositoryInterface
import com.example.mcommerce.orders.model.OrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PaymentViewModel (repo: RepositoryInterface) : ViewModel() {
    private val iRepo: RepositoryInterface = repo
    private val newOrder = MutableLiveData<Response<OrderResponse>>()

    val onlineNewOrder : LiveData<Response<OrderResponse>> = newOrder

    fun postNewOrder(orderResponse: OrderResponse){
        viewModelScope.launch{
            val result = iRepo.postNewOrder(orderResponse)
            withContext(Dispatchers.Main){
                newOrder.value= result
              //  Log.i("TAG","result: $result")
            }
        }

    }

}