package com.example.mcommerce.orders.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import com.example.mcommerce.orders.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allOrders = MutableLiveData<List<Order>>()
    val allOnlineOrders: LiveData<List<Order>> = allOrders
    fun getAllOrders(id:String) {
        viewModelScope.launch {
            val result = iRepo.getOrders(id)
            withContext(Dispatchers.Main) {
                Log.i("TAG", "all orders: ${result.orders}")
                allOrders.postValue(result.orders)
            }
        }
    }

}
