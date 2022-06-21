package com.example.mcommerce.orders.viewModel

import android.os.Looper.getMainLooper
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
class RepositoryTest() : TestCase() {
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
    fun getOrders() = runBlockingTest {
        // When tasks are requested from the tasks repository
        val tasks = repo.getOrders("")

        // Then tasks are loaded from the remote data source
        assertEquals(2,tasks.orders.size)
    }

    /*private val testDispatcher = TestCoroutineDispatcher()
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
        shadowOf(getMainLooper()).idle();

        //val result = mockRepo.getOrders("5758070096011")

        // assert your case
        //assertEquals(result.orders.size,5)
        assertNotNull(mockRepo)

    }*/

}