package com.example.mcommerce.me.viewmodel

import android.content.Context
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.FakeRepository
import com.example.mcommerce.model.Repository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode


@Config(sdk =[30])
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@ExperimentalCoroutinesApi
class CustomerViewModelTest : TestCase(){

    private lateinit var remoteDataSource: FakeRepository
    private lateinit var repo: Repository
    val customerXObj = CustomerX(
        first_name = "Asmaa", last_name = "Youssef", currency = "EGP",
        email = "asmaayoussef786@gmail.com", phone = "01275280853", tags = "asmaa257")

    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        remoteDataSource = FakeRepository()
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getUserDetails_usingUId_userDetails() = runBlockingTest {
        val viewModel = CustomerViewModel(repo)
        viewModel.getUserDetails("")
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val tasks = viewModel.customerInfo.getOrAwaitValue()
        assertEquals(customerXObj,tasks)
    }

    @Test
    fun getAllCurrencies_allUserCurrencies() = runBlockingTest {
        val viewModel = CustomerViewModel(repo)
        viewModel.getAllCurrencies()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val tasks = viewModel.onlineCurrencies.getOrAwaitValue()
        assertEquals(2,tasks.size)
    }

}