package com.example.mcommerce.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private var allProducts = MutableLiveData<List<Product>>()
    private var allFavProducts = MutableLiveData<List<DraftOrderX>>()
    private val itemDeleted = MutableLiveData<Response<DraftOrder>>()
    private val cardOrder = MutableLiveData<Response<DraftOrder>>()

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

    }

}
