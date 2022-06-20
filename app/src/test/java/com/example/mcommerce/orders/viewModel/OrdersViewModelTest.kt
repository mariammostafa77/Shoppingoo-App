package com.example.mcommerce.orders.viewModel

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.model.Repository
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk =[30])
@RunWith(AndroidJUnit4::class)
class OrdersViewModelTest() : TestCase() {

    //private lateinit var ordersViewModel: OrdersViewModel
    override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        //val repo = Repository(,context)
    }

    @Test
    fun signInTest() {
        assertEquals(4,2+2)
    }

}