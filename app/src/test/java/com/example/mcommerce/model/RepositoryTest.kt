package com.example.mcommerce.orders.viewModel

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.auth.model.Customer
import com.example.mcommerce.auth.model.CustomerX
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.DraftResponse
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.*
import com.google.android.gms.common.util.CollectionUtils
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.example.mcommerce.model.currencies.CurrencyModel
import com.example.mcommerce.model.currencies.CurrencyResponse
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@ExperimentalCoroutinesApi
class RepositoryTest() : TestCase() {
    private lateinit var remoteDataSource: FakeRepository
    private lateinit var repo: Repository

    //zeinab
    var customer = CustomerX(
        first_name = "Zeinab",
        last_name = "Ibrahim",
        email = "zeinabibrahim541@gmail.com",
        verified_email = true,
        phone = "01143232215",
        tags = "123456789",
        addresses = listOf(
            Addresse(
                address1 = "Alkafal",
                phone = "01203574583",
                city = "Alex",
                province = "",
                zip = "21552",
                last_name = "Lastnameson",
                first_name = "Mother",
                country = "EG"
            )
        )
    )

    var myCustomer = Customer(CollectionUtils.listOf(customer))

        var product= Product(id = 123456,title = "Adidas",tags = "tags")
       var myProdct= ProductDetails(product)

    lateinit var context: Context
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    val discountCode1 = DiscountCode(code = "SUMERSALE2022", "", 1, 12345, "", 0)
    val discountCode2 = DiscountCode(code = "SALE2022", "", 2, 12345, "", 0)
    val discount_codes_list = DiscountCodesModel(listOf(discountCode1, discountCode2))

    val customerXObj = CustomerX(
        first_name = "Asmaa", last_name = "Youssef", currency = "EGP",
        email = "asmaayoussef786@gmail.com", phone = "01275280853", tags = "asmaa257")
    val customerDetail = CustomerDetail(customerXObj)

    val draftOrderX1 = DraftOrderX(
        email = "asmaaYoussef786@gmail.com",
        id = 1234567,
        note = "cart",
        total_price = "100",
        total_tax = "10"
    )
    val draftOrderX2 = DraftOrderX(
        email = "asmaaYoussef786@gmail.com",
        id = 12345,
        note = "cart",
        total_price = "100",
        total_tax = "10"
    )
    val draftResponse = DraftResponse(listOf(draftOrderX1, draftOrderX2))
    val currencyModel1 = CurrencyModel(currency = "EGP", enabled = true)
    val currencyModel2 = CurrencyModel(currency = "USD", enabled = false)
    val currencyResponse = CurrencyResponse(listOf(currencyModel1, currencyModel2))

    @Before
    fun createRepository() {
        remoteDataSource = FakeRepository()
        context = getApplicationContext<Context>()
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getDiscountCoupons_allCoupons() = testScope.runBlockingTest {
        assertEquals(repo.getDiscountsCods().discount_codes, discount_codes_list.discount_codes)
    }

    @Test
    fun getUserDetails_userId_customer() = testScope.runBlockingTest {
        assertEquals(repo.getCustomerDetails("123456").customer, customerDetail.customer)
    }

    @Test
    fun getShoppingCartProducts_allProducts() = testScope.runBlockingTest {
        assertEquals(repo.getShoppingCartProducts().draft_orders, draftResponse.draft_orders)
    }

    @Test
    fun getCustomerTest_allCustomer() = runBlockingTest {
        assertEquals(repo.getCustomers().customers, myCustomer.customers)
    }

    @Test
    fun getAllCurrencies_allCurrencies() = testScope.runBlockingTest {
        assertEquals(repo.getAllCurrencies().currencies, currencyResponse.currencies)
    }

    @Test
    fun getOrdersFromViewModel() = runBlockingTest {
        val tasks = repo.getOrders("")

        // Then tasks are loaded from the remote data source
        assertEquals(2,tasks.orders.size)
    }
    @Test
    fun getSpecificProduct_specificProduct()= testScope.runBlockingTest {

         assertEquals(repo.getSpecificProduct("6870135275659").product,myProdct.product)


    }

}


