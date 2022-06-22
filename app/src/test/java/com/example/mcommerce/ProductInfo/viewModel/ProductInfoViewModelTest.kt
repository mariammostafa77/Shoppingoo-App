package com.example.mcommerce.ProductInfo.viewModel

import junit.framework.TestCase
import android.content.Context
import android.os.Looper.getMainLooper
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.auth.login.viewModel.LoginViewModel
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.model.FakeRepository
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.model.Orders
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import java.util.Observer


@Config(sdk =[30])
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@ExperimentalCoroutinesApi
class ProductInfoViewModelTest : TestCase(){
    private lateinit var remoteDataSource: FakeRepository
    private lateinit var repo: Repository

    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        remoteDataSource = FakeRepository()
        // Get a reference to the class under test
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getDraftOrdersTest_allDraftOrders() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val viewModel = ProductInfoViewModel(repo)
        viewModel.getFavProducts()
        shadowOf(getMainLooper()).idle();

        val tasks = viewModel.onlineFavProduct.getOrAwaitValue()

        // Then tasks are loaded from the remote data source
        assertEquals(2,tasks.size)
    }
    @Test
    fun getSpecificProducts_specificProduct() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val viewModel = ProductInfoViewModel(repo)
        viewModel.getSpecificProducts("6870135275659")
        shadowOf(getMainLooper()).idle();

        val tasks = viewModel.onlineSpecificProducts.getOrAwaitValue()

        // Then tasks are loaded from the remote data source
        assertEquals("Adidas",tasks.title)
    }


}