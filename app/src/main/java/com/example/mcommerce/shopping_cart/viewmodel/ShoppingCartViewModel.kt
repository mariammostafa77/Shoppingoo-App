package com.example.mcommerce.shopping_cart.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.model.RepositoryInterface
import com.example.mcommerce.orders.model.OrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ShoppingCartViewModel(repo: RepositoryInterface) : ViewModel() {

    private val iRepo: RepositoryInterface = repo
    private var shopingCartProducts = MutableLiveData<List<DraftOrderX>>()
    private val itemDeleted = MutableLiveData<Response<DraftOrder>>()
    private val itemUpdated = MutableLiveData<Response<DraftOrder>>()
    private val newOrder = MutableLiveData<Response<OrderResponse>>()

    val onlineShoppingCartProduct: LiveData<List<DraftOrderX>> = shopingCartProducts

    val selectedItem : MutableLiveData<Response<DraftOrder>> = itemDeleted
    val onlineItemUpdated : MutableLiveData<Response<DraftOrder>> = itemUpdated
    val onlineNewOrder : LiveData<Response<OrderResponse>> = newOrder


    fun getShoppingCardProducts(){
        viewModelScope.launch{
            val result = iRepo.getShoppingCartProducts()
            withContext(Dispatchers.Main){
                shopingCartProducts.postValue(result.draft_orders)
            }
        }
    }
    fun deleteSelectedProduct(id: String){
        viewModelScope.launch{
            val result = iRepo.deleteProductFromShoppingCart(id)
            withContext(Dispatchers.Main){
                itemDeleted.value = result
            }
        }
    }

    fun updateSelectedProduct(id: String,order: DraftOrder){
        viewModelScope.launch{
            val result = iRepo.updateDraftOrder(id,order)
            withContext(Dispatchers.Main){
                itemUpdated.value = result
            }
        }
    }

    fun postNewOrder(orderResponse: OrderResponse){
        viewModelScope.launch{
            val result = iRepo.postNewOrder(orderResponse)
            withContext(Dispatchers.Main){
                newOrder.value= result
            }
        }

    }



}