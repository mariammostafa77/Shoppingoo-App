package com.example.mcommerce.brandProducts.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import com.example.mcommerce.model.Variant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrandProductsViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allBrandProducts = MutableLiveData<List<Product>>()

    val onlineBrandProducts: LiveData<List<Product>> = allBrandProducts
    fun getBrandProducts(id:String){
        viewModelScope.launch{
            val result = iRepo.getBrandProducts(id)
            withContext(Dispatchers.Main){
                Log.i("TAG",result.toString())
                allBrandProducts.postValue(result.products)

            }
        }

    }


}
