package com.example.mcommerce.home.viewModel

import junit.framework.TestCase
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk=[30])
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest : TestCase(){

/*
    lateinit var homeFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    @Before
    override fun setUp() {
        super.setUp()
        val context = getApplicationContext<Context>()
         viewModel = HomeViewModel(Repository.getInstance(AppClient.getInstance(),context))
    }
*/

    @Test
    fun getDiscountCoupons_allCoupons(){
        val context = getApplicationContext<Context>()
        val viewModel = HomeViewModel(Repository.getInstance(AppClient.getInstance(),context))
        viewModel.getDiscountCoupons()
       // val value = viewModel.onlineDiscountCodes.value
        val value = viewModel.onlineDiscountCodes.getOrAwaitValue{}
        assertNotNull(value)
    }

}