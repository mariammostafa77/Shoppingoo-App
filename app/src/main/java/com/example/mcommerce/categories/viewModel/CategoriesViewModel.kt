package com.example.mcommerce.categories.viewModel

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

class CategoriesViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allProducts = MutableLiveData<List<Product>>()
    val onlineProducts: LiveData<List<Product>> = allProducts
    fun getAllProducts(id:String){
        viewModelScope.launch{
            val result = iRepo.getBrandProducts(id)
            withContext(Dispatchers.Main){
                Log.i("TAG",result.toString())
                allProducts.postValue(result.products)

            }
        }

    }

}
