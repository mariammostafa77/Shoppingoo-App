package com.example.mcommerce.ProductInfo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductInfoViewModel (repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val specificProducts = MutableLiveData<Product>()
    private val cardOrder = MutableLiveData<Response<DraftOrder>>()
    private var favProducts = MutableLiveData<List<DraftOrderX>>()
    private val itemDeleted = MutableLiveData<Response<DraftOrder>>()


    //Expose returned online Data
    val onlineSpecificProducts: LiveData<Product> = specificProducts
    val onlineCardOrder: LiveData<Response<DraftOrder>> = cardOrder
    fun getSpecificProducts(id:String){
        viewModelScope.launch{
            val result = iRepo.getSpecificProduct(id)
            //Log.i("pro","from model"+result)
            withContext(Dispatchers.Main){

                specificProducts.postValue(result.product)

            }
        }

    }
    fun getCardOrder(order: DraftOrder){
        viewModelScope.launch{
            val result = iRepo.postNewDraftOrder(order)
            //Log.i("pro","from model"+result)
            withContext(Dispatchers.Main){

                cardOrder.value=result

            }
        }

    }
    val onlineFavProduct: LiveData<List<DraftOrderX>> = favProducts
    val selectedItem : MutableLiveData<Response<DraftOrder>> = itemDeleted
    fun getFavProducts(){
        viewModelScope.launch{
            val result = iRepo.getShoppingCartProducts()
            withContext(Dispatchers.Main){
                favProducts.postValue(result.draft_orders)
            }
        }
    }
    fun deleteFavProduct(id: String){
        viewModelScope.launch{
            val result = iRepo.deleteProductFromShoppingCart(id)
            withContext(Dispatchers.Main){
                itemDeleted.value = result
            }
        }

    }
}
