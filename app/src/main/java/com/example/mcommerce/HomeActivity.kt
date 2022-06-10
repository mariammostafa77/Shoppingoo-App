package com.example.mcommerce

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.ProductInfo.view.ProductInfoFragment
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.home.view.HomeFragment
import com.example.mcommerce.me.view.MeWithLogin
import com.example.mcommerce.me.view.MeWithoutLoginFragment
import com.example.mcommerce.me.view.setting.AddNewAddressFragment
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Product
import com.example.mcommerce.orderDetails.view.OrderDetailsFragment
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.search.view.MysearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeActivity : AppCompatActivity(),Communicator {
    private val homeFragment = HomeFragment()
    private val meWithLogin = MeWithLogin()
    private val categoryFragment = CategoryFragment()
    private val meWithoutLoginFragment = MeWithoutLoginFragment()
    lateinit var bottomNavigationView: BottomNavigationView

    companion object{
        var mySearchFlag:Int=0
        var myDetailsFlag:Int=0
        var myFavFlag:Boolean=false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val favSharedPreferences = getSharedPreferences("favourite", AppCompatActivity.MODE_PRIVATE)
       myFavFlag= favSharedPreferences.getBoolean("favStatue",false)


        SavedSetting.loadLocale(this)



        bottomNavigationView = findViewById(R.id.buttomNav)

       // replaceFragment(homeFragment)

        //getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout,new HomeFragment()).commit();
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frameLayout) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val navGraph = navHostFragment!!.navController.navInflater.inflate(R.navigation.my_nav_graph)
        // navGraph.setStartDestination(R.id.fragmentAddMed1);
        // navGraph.setStartDestination(R.id.fragmentAddMed1);
        navGraph.setStartDestination(R.id.homeFragment)
        navController.graph = navGraph

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeTab -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.categoryTab -> {
                    replaceFragment(categoryFragment)
                    true
                }
                R.id.meTab -> {
                    replaceFragment(meWithLogin)
                    true
                }
                else -> false
            }
        }
        //from search to productInfo

        passMapDataToFragment()
    }


    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }

    override fun passProductData(product: Product) {
       val bundle=Bundle()
        bundle.putSerializable("productInfo",product)
        val transaction=this.supportFragmentManager.beginTransaction()
        val productInfoFragment=ProductInfoFragment()
        productInfoFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,productInfoFragment).commit()
    }

    override fun goFromBrandToCategories(brandName:String) {
        val bundle=Bundle()
        bundle.putString("brandTitle",brandName)
        categoryFragment.arguments=bundle
        replaceFragment(categoryFragment)
        Log.i("TAG","brandName from home $brandName")
    }
    override fun goToSearchWithID(id: String) {
        val bundle=Bundle()
        bundle.putString("catID",id)
        val transaction=this.supportFragmentManager.beginTransaction()
        val searchFragment=MysearchFragment()
        searchFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,searchFragment).commit()
    }

    override fun goToSearchWithAllData(id: String, brandName: String, subCatName: String) {
        val bundle=Bundle()
        bundle.putString("catID",id)
        bundle.putString("brandName",brandName)
        bundle.putString("subCatName",subCatName)
        val transaction=this.supportFragmentManager.beginTransaction()
        val searchFragment=MysearchFragment()
        searchFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,searchFragment).commit()
    }

    override fun goToUserAddresses() {
        TODO("Not yet implemented")
    }

    override fun goToPaymentFromAddress(selectedAddress: Addresse) {
        TODO("Not yet implemented")
    }

    override fun goToOrderDetails(selectedOrder: Order) {
        val bundle=Bundle()
        bundle.putSerializable("selectedOrder",selectedOrder)
        val orderDetailsFragment=OrderDetailsFragment()
        orderDetailsFragment.arguments=bundle
        replaceFragment(orderDetailsFragment)
    }

    private fun passMapDataToFragment() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val userAdress = intent.getStringArrayListExtra("userAddress")
            Log.i("Testttt","from Home Activity ${userAdress?.get(0)}")

            val mBundle = Bundle()
            var addressFragment = AddNewAddressFragment()
            mBundle.putStringArrayList("adress",userAdress)
            addressFragment.arguments = mBundle
            replaceFragment(addressFragment)

        }
    }


}