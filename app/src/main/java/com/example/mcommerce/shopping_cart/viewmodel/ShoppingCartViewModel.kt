package com.example.mcommerce.shopping_cart.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingCartViewModel(repo: RepositoryInterface) : ViewModel() {

    private val iRepo: RepositoryInterface = repo
    private var shopingCartProducts = MutableLiveData<List<DraftOrderX>>()

    val onlineShoppingCartProduct: LiveData<List<DraftOrderX>> = shopingCartProducts

    fun getShoppingCardProducts(){
        viewModelScope.launch{
            val result = iRepo.getShoppingCartProducts()
            withContext(Dispatchers.Main){
                shopingCartProducts.postValue(result.draft_orders)
                Log.i("cart", "Shopping Cart From ViewModel ${result}")
            }
        }

    }




}