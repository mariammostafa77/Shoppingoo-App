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
    val onlineFavProduct: LiveData<List<DraftOrderX>> = allFavProducts
    val selectedItem : MutableLiveData<Response<DraftOrder>> = itemDeleted

    fun getFavProducts(){
        viewModelScope.launch{
            val result = iRepo.getShoppingCartProducts()
            withContext(Dispatchers.Main){
                allFavProducts.postValue(result.draft_orders)
            }
        }
    }
    fun deleteSelectedProduct(id: String){
        viewModelScope.launch{
            val result = iRepo.deleteProductFromShoppingCart(id)
            withContext(Dispatchers.Main){
                itemDeleted.value = result
            }
        }

    }
    val onlineCardOrder: LiveData<Response<DraftOrder>> = cardOrder
    fun getCardOrder(order: DraftOrder){
        viewModelScope.launch{
            val result = iRepo.postNewDraftOrder(order)
            //Log.i("pro","from model"+result)
            withContext(Dispatchers.Main){

                cardOrder.value=result

            }
        }

    }
}
