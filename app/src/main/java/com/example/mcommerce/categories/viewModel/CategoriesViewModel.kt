package com.example.mcommerce.categories.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.model.*
import com.example.mcommerce.model.currencies.convertor.CurrencyConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allProducts = MutableLiveData<List<Product>>()
    val allOnlineProductsSubTypes: LiveData<List<Product>> = allProducts
    fun getSubType(vendor:String,productType:String,collectionId:String) {
        viewModelScope.launch {
            val result = iRepo.getSubCategories(vendor, productType, collectionId)
            withContext(Dispatchers.Main) {
                allProducts.postValue(result.products)

            }
        }
    }

    private val subcategoriesProduct = MutableLiveData<List<Product>>()
    val onlinesubcategoriesProduct: LiveData<List<Product>> = subcategoriesProduct
    fun getCategoriesProduct(vendor:String,productType:String,collectionId:String){
        viewModelScope.launch{
            val result = iRepo.getSubCategories(vendor,productType,collectionId)
            withContext(Dispatchers.Main){
                subcategoriesProduct.postValue(result.products)

            }
        }
    }

    private val currencyChangedInEgp = MutableLiveData<CurrencyConverter>()
    val onlineCurrencyChangedInEgp: LiveData<CurrencyConverter> = currencyChangedInEgp
    fun getAmountAfterConversionInEgp(from: String){
        viewModelScope.launch {
            val result = iRepo.getCurrencyValueInEgp(from)
            withContext(Dispatchers.Main) {
                currencyChangedInEgp.postValue(result)
            }
        }
    }

}
