package com.example.mcommerce.confirmOrder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ConfirmOrderViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val deletedItem = MutableLiveData<Response<DraftOrder>>()
    val selectedDeletedItem : MutableLiveData<Response<DraftOrder>> = deletedItem

    fun deleteOrderProducts(id: String){
        viewModelScope.launch{
            val result = iRepo.deleteProductFromShoppingCart(id)
            withContext(Dispatchers.Main){
                deletedItem.value = result
            }
        }
    }
}
