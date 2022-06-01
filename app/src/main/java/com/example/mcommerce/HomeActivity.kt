package com.example.mcommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.ProductInfo.view.ProductInfoFragment
import com.example.mcommerce.brandProducts.view.BrandProductsFragment
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.home.view.HomeFragment
import com.example.mcommerce.model.Product
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(),Communicator {
    private val homeFragment = HomeFragment()
    private val meWithLogin = MeWithLogin()
    private val categoryFragment = CategoryFragment()
    private  val brandProductsFragment = BrandProductsFragment()
    private val meWithoutLoginFragment = MeWithoutLoginFragment()
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
                    replaceFragment(meWithoutLoginFragment)
                    true
                }
                else -> false
            }
        }
        //from search to productInfo


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

    override fun goToProductInfo(id: String) {
        val bundle=Bundle()
        bundle.putString("brandId",id)
        brandProductsFragment.arguments=bundle
        replaceFragment(brandProductsFragment)
    }


}