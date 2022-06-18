package com.example.mcommerce.categories.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(repo: RepositoryInterface) : ViewModel(){
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
    /*private val allProductTypes = MutableLiveData<List<Product>>()
    val onlineProductsTypes: LiveData<List<Product>> = allProductTypes
    fun getProductsType(id:String){
        viewModelScope.launch{
            val result = iRepo.getProductTypes(id)
            withContext(Dispatchers.Main){
                Log.i("TAG",result.toString())
                allProductTypes.postValue(result.products)
                Log.i("TAG","variant ${result.products[0].product_type}")

            }
        }
    }*/

    private val subcategoriesProduct = MutableLiveData<List<Product>>()
    val onlinesubcategoriesProduct: LiveData<List<Product>> = subcategoriesProduct
    fun getCategories(vendor:String,productType:String,collectionId:String){
        viewModelScope.launch{
            val result = iRepo.getSubCategories(vendor,productType,collectionId)
            withContext(Dispatchers.Main){
                Log.i("subcategoriesProduct",result.toString())
                subcategoriesProduct.postValue(result.products)

            }
        }
    }

}
