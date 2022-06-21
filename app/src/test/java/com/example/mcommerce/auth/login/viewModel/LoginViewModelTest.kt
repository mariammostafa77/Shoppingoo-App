package com.example.mcommerce.auth.login.viewModel

import junit.framework.TestCase
import android.os.Looper.getMainLooper
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.Repository
import com.example.mcommerce.orders.model.Orders
import com.example.mcommerce.orders.viewModel.OrdersViewModel
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

class LoginViewModelTest : TestCase() {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }
    @Test
    fun getCustomerTest_allCustomer()= testScope.runBlockingTest {
        val mockRepo = mock<Repository> {
            onBlocking { getCustomers() } doReturn Customer(listOf())
        }
        val viewModel = LoginViewModel(mockRepo)
        viewModel.getCustomer()
        shadowOf(getMainLooper()).idle();

        val result = viewModel.customer.getOrAwaitValue()

        assertNotNull(result)

    }


}