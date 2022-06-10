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

    val onlineProducts: LiveData<List<Product>> = allProducts

    fun getBrandProducts(id:String){
        viewModelScope.launch{
            val result = iRepo.getBrandProducts(id)
            withContext(Dispatchers.Main){
                Log.i("TAG",result.toString())
                allProducts.postValue(result.products)

            }
        }

    }
    private val allVariantProducts = MutableLiveData<Variants>()
    val onlineVariantProducts: LiveData<Variants> = allVariantProducts
    fun getVariants(id:String){
        viewModelScope.launch{
            val result = iRepo.getVariant(id)
            withContext(Dispatchers.Main){
                Log.i("TAG",result.toString())
                allVariantProducts.postValue(result)
                Log.i("TAG","variant $allVariantProducts")

            }
        }
    }

    private val subcategoriesProduct = MutableLiveData<List<Product>>()
    val onlinesubcategoriesProduct: LiveData<List<Product>> = subcategoriesProduct
    fun getCategories(vendor:String,productType:String,collectionId:String){
        viewModelScope.launch{
            val result = iRepo.getSubCategories(vendor,productType,collectionId)
            withContext(Dispatchers.Main){
                Log.i("subcategoriesProduct",result.toString())
                subcategoriesProduct.postValue(result.products)
                Log.i("TAG","subcategoriesProduct $allVariantProducts")

            }
        }
    }
}
