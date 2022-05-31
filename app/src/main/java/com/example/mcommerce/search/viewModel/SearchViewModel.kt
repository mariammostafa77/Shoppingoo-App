package com.example.mcommerce.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private var allProducts = MutableLiveData<List<Product>>()

    init {
        getAllProducts()
    }

    //Expose returned online Data
    val onlineProducts: LiveData<List<Product>> = allProducts
    fun getAllProducts(){
        viewModelScope.launch{
            val result = iRepo.getAllProducts()
            withContext(Dispatchers.Main){
                Log.i("TAG","from home model view ${result.products[1].id}")

                allProducts.postValue(result.products)


            }
        }
        //Log.i("TAG","from home model view ${allProducts.value}")
        //Log.i("TAG","from home model view ${onlineProducts.value}")
    }

}
