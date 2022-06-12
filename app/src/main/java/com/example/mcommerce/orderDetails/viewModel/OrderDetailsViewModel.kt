package com.example.mcommerce.orderDetails.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allProducts = MutableLiveData<List<Product>>()
    val allOnlineProducts: LiveData<List<Product>> = allProducts
    fun getAllProducts(vendor:String,productType:String,collectionId:String) {
        viewModelScope.launch {
            val result = iRepo.getSubCategories(vendor, productType, collectionId)
            withContext(Dispatchers.Main) {
                Log.i("subcategoriesProduct", result.toString())
                allProducts.postValue(result.products)

            }
        }
    }

}
