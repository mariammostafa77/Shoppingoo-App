package com.example.mcommerce.orders.viewModel

import android.os.Looper.getMainLooper
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.Repository
import com.example.mcommerce.orders.model.Orders
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.TestCase
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
class OrdersViewModelTest() : TestCase() {

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
    fun getAllOrders_allOrder() = testScope.runBlockingTest {
        val mockRepo = mock<Repository> {
            onBlocking { getOrders("5758070096011") } doReturn Orders(listOf())
        }
        val viewModel = OrdersViewModel(mockRepo)
        viewModel.getAllOrders("5758070096011")
        shadowOf(getMainLooper()).idle();

        val result = viewModel.allOnlineOrders.getOrAwaitValue()

        // assert your case
        //assertEquals(result.size,5)
        assertNotNull(result)

    }
}