package com.example.mcommerce.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mcommerce.home.model.BrandsModel
import com.example.mcommerce.network.RemoteSourceInterface

class Repository private constructor(var remoteSource: RemoteSourceInterface, var context: Context)
    : RepositoryInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSourceInterface,
            context: Context
        ): Repository {
            return instance ?: Repository(
                remoteSource,context
            )
        }
    }
    override suspend fun getAllProducts(): AllProductsModel {
        return remoteSource.getAllProducts()
    }

    override suspend fun getAllBrands(): BrandsModel {
        return remoteSource.getAllBrands()
    }

    override suspend fun getBrandProducts(): AllProductsModel {
        return remoteSource.getBrandProducts()
    }

    override suspend fun getDiscountsCods(): DiscountCodesModel {
        return  remoteSource.getDiscountCodes()
    }

}