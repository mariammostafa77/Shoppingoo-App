package com.example.mcommerce.categories.viewModel

import android.content.Context
import android.os.Looper.getMainLooper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mcommerce.getOrAwaitValue
import com.example.mcommerce.model.FakeRepository
import com.example.mcommerce.model.Repository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
class CategoriesViewModelTest() : TestCase() {
    private lateinit var remoteDataSource: FakeRepository
    private lateinit var repo: Repository

    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        remoteDataSource = FakeRepository()
        repo = Repository.getInstance(remoteDataSource, context)
    }

    @Test
    fun getCategoriesProduct_threeProducts() = runBlockingTest {
        val viewModel = CategoriesViewModel(repo)
        viewModel.getCategoriesProduct("","","")
        shadowOf(getMainLooper()).idle();
        val tasks = viewModel.onlinesubcategoriesProduct.getOrAwaitValue()
        assertEquals(3,tasks.size)
    }


}