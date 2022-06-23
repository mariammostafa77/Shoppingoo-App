package com.example.mcommerce.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeViewModel(repo: RepositoryInterface) : ViewModel(){
    private val iRepo: RepositoryInterface = repo
    private val allBrands = MutableLiveData<List<SmartCollection>>()
    private val discountCode = MutableLiveData<List<DiscountCode>>()
    val onlineDiscountCodes: LiveData<List<DiscountCode>> = discountCode
    val onlineBrands: LiveData<List<SmartCollection>> = allBrands
    fun getAllBrands(){
        viewModelScope.launch{
            val result = iRepo.getAllBrands()
            withContext(Dispatchers.Main){
                allBrands.postValue(result.smart_collections)
            }
        }
    }

    //// Discount Codes
    fun getDiscountCoupons(){
        viewModelScope.launch{
            val result = iRepo.getDiscountsCods()
            withContext(Dispatchers.Main){
                discountCode.postValue(result.discount_codes)
            }
        }
    }






}
