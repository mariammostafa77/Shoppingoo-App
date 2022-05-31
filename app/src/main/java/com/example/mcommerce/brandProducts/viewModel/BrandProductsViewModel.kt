package com.example.mcommerce.brandProducts.viewModel

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

class BrandProductsViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allBrandProducts = MutableLiveData<List<Product>>()

    init {
        getAllProducts()
    }

    //Expose returned online Data
    val onlineBrandProducts: LiveData<List<Product>> = allBrandProducts
    fun getAllProducts(){
        viewModelScope.launch{
            val result = iRepo.getBrandProducts()
            withContext(Dispatchers.Main){
                Log.i("TAG","from home model view ${result.products[1].id}")
                allBrandProducts.postValue(result.products)

            }
        }

    }

}
