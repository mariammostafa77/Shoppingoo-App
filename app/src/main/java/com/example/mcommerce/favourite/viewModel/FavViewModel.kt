package com.example.mcommerce.favourite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavViewModel (repo: RepositoryInterface) : ViewModel() {

    private val iRepo: RepositoryInterface = repo
    private var allFavProducts = MutableLiveData<List<DraftOrderX>>()
    private val itemDeleted = MutableLiveData<Response<DraftOrder>>()

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




}