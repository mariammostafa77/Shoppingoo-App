package com.example.mcommerce.home.viewModel

import junit.framework.TestCase
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import android.os.Looper.getMainLooper
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.DiscountCodesModel
import com.example.mcommerce.model.Repository
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


@Config(sdk =[30])
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@ExperimentalCoroutinesApi
class HomeViewModelTest : TestCase(){

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
    fun getDiscountCoupons_allCoupons() = testScope.runBlockingTest {
        val mockRepo = mock<Repository> {
            onBlocking { getDiscountsCods() } doReturn DiscountCodesModel(listOf())
        }
        val viewModel = HomeViewModel(mockRepo)
        viewModel.getDiscountCoupons()
        shadowOf(getMainLooper()).idle();
        val result = viewModel.onlineDiscountCodes.getOrAwaitValue()
        assertNotNull(result)
    }

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
        /*
    @Test
    fun getDiscountCoupons_allCoupons(){
        val context = getApplicationContext<Context>()
        val viewModel = HomeViewModel(Repository.getInstance(AppClient.getInstance(),context))
        viewModel.getDiscountCoupons()
       // val value = viewModel.onlineDiscountCodes.value
        val value = viewModel.onlineDiscountCodes.getOrAwaitValue{}
        assertNotNull(value)
    }
*/
}