package com.example.mcommerce.ProductInfo.viewModel

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

class ProductInfoViewModel (repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val specificProducts = MutableLiveData<Product>()


    //Expose returned online Data
    val onlineSpecificProducts: LiveData<Product> = specificProducts
    fun getSpecificProducts(id:String){
        viewModelScope.launch{
            val result = iRepo.getSpecificProduct(id)
            //Log.i("pro","from model"+result)
            withContext(Dispatchers.Main){

                specificProducts.postValue(result.product)

            }
        }

    }

}
