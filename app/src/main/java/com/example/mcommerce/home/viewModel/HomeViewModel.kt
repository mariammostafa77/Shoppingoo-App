package com.example.mcommerce.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.AllProductsModel
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allBrands = MutableLiveData<List<SmartCollection>>()

    //Expose returned online Data
    val onlineBrands: LiveData<List<SmartCollection>> = allBrands
    fun getAllProducts(){
        viewModelScope.launch{
            val result = iRepo.getAllBrands()
            withContext(Dispatchers.Main){
                Log.i("TAG","from home model view ${result.smart_collections[1].id}")
                allBrands.postValue(result.smart_collections)

            }
        }
        //Log.i("TAG","from home model view ${allProducts.value}")
        //Log.i("TAG","from home model view ${onlineProducts.value}")
    }

}
