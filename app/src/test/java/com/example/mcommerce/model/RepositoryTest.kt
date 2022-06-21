package com.example.mcommerce.orders.viewModel

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.FakeRepository
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.ProductDetails
import com.example.mcommerce.model.Repository
import com.example.mcommerce.orders.model.Orders
import com.google.android.gms.common.util.CollectionUtils
import com.google.android.gms.common.util.CollectionUtils.listOf
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
class RepositoryTest() : TestCase() {
    private lateinit var remoteDataSource: FakeRepository
    private lateinit var repo: Repository
    //zeinab
    var customer = CustomerX(first_name = "Zeinab",last_name = "Ibrahim",email = "zeinabibrahim541@gmail.com"
        ,verified_email = true,phone = "01143232215",tags = "123456789",
        addresses = listOf(Addresse(address1 = "Alkafal",
            phone = "01203574583",
            city = "Alex",
            province = "",
            zip = "21552",
            last_name = "Lastnameson",
            first_name = "Mother",
            country = "EG"))
    )

   var myCustomer= Customer(CollectionUtils.listOf(customer))
//    var product= Product(tags = "tags")
//    var myProdct= ProductDetails(product)

    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        remoteDataSource = FakeRepository()
        // Get a reference to the class under test
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getOrders() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val tasks = repo.getOrders("")

        // Then tasks are loaded from the remote data source
        assertEquals(2,tasks.orders.size)
    }

    @Test
    fun getCustomerTest_allCustomer()=  runBlockingTest {

        assertEquals(repo.getCustomers().customers,myCustomer.customers)



    }
//    @Test
//    fun getSpecificProduct_specificProduct()= testScope.runBlockingTest {
//
//        assertEquals(repo.getSpecificProduct("6870135275659").product,myProdct.product)
//
//    }

}