package com.example.mcommerce

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.ProductInfo.view.ProductInfoFragment
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.confirmOrder.view.ConfirmOrderFragment
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.OrderPrices
import com.example.mcommerce.home.view.HomeFragment
import com.example.mcommerce.me.view.MeWithLogin
import com.example.mcommerce.me.view.MeWithoutLoginFragment
import com.example.mcommerce.me.view.setting.AddNewAddressFragment
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Product
import com.example.mcommerce.orderDetails.view.OrderDetailsFragment
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.search.view.MysearchFragment
import com.example.mcommerce.payment.view.PaymentAddressFragment
import com.example.mcommerce.payment.view.PaymentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeActivity : AppCompatActivity(),Communicator {
    private val homeFragment = HomeFragment()
    private val meWithLogin = MeWithLogin()
    private val myCategoryFragment = CategoryFragment(1)
    private val meWithoutLoginFragment = MeWithoutLoginFragment()
    lateinit var bottomNavigationView: BottomNavigationView
    private var userId =""

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

        val sharedPreferences: SharedPreferences = getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("cusomerID","").toString()

        bottomNavigationView = findViewById(R.id.buttomNav)

        replaceFragment(homeFragment)

        //getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout,new HomeFragment()).commit();


//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.frameLayout) as NavHostFragment?
//        val navController = navHostFragment!!.navController
//        val navGraph = navHostFragment!!.navController.navInflater.inflate(R.navigation.my_nav_graph)


        // navGraph.setStartDestination(R.id.fragmentAddMed1);
        // navGraph.setStartDestination(R.id.fragmentAddMed1);

//        navGraph.setStartDestination(R.id.homeFragment)
//        navController.graph = navGraph

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeTab -> {
                    //removeFragment(get)
                    replaceFragment(homeFragment)
                    true
                }
                R.id.categoryTab -> {
                   // finish()
                    replaceFragment(myCategoryFragment)
                    true
                }
                R.id.meTab -> {
                   // finish()
                    if(userId.isNullOrEmpty()){
                        replaceFragment(meWithoutLoginFragment)
                    }else{
                        replaceFragment(meWithLogin)
                    }
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
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun removeFragment(fragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }

    override fun passProductData(product: Product) {
        myDetailsFlag=0
       val bundle=Bundle()
        bundle.putSerializable("productInfo",product)
        val transaction=this.supportFragmentManager.beginTransaction()
        val productInfoFragment=ProductInfoFragment()
        productInfoFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,productInfoFragment).commit()
    }

    override fun goFromBrandToCategories(brandName:String) {
        myDetailsFlag=0
        val bundle=Bundle()
        bundle.putString("brandTitle",brandName)
        val categoryFragment = CategoryFragment(0)
        categoryFragment.arguments=bundle
        replaceFragment(categoryFragment)
        //bottomNavigationView.setSelectedItemId(R.id.categoryTab);
        Log.i("TAG","brandName from home $brandName")
    }
    override fun goToSearchWithID(id: String) {
        myDetailsFlag=0
        val bundle=Bundle()
        bundle.putString("catID",id)
        val transaction=this.supportFragmentManager.beginTransaction()
        val searchFragment=MysearchFragment()
        searchFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,searchFragment).commit()
    }

    override fun goToSearchWithAllData(id: String, brandName: String, subCatName: String) {
        myDetailsFlag=0
        val bundle=Bundle()
        bundle.putString("catID",id)
        bundle.putString("brandName",brandName)
        bundle.putString("subCatName",subCatName)
        val transaction=this.supportFragmentManager.beginTransaction()
        val searchFragment=MysearchFragment()
        searchFragment.arguments=bundle
        transaction.replace(R.id.frameLayout,searchFragment).commit()
    }

    override fun goToUserAddresses(lineItems: ArrayList<LineItem>, orderPrices: ArrayList<OrderPrices> ){
        myDetailsFlag=0
        val bundle=Bundle()
       // val userAddressesFragment = UserAddressesFragment()
        val paymentAddressFragment = PaymentAddressFragment()
        bundle.putSerializable("line_items",lineItems)
        bundle.putSerializable("order_price",orderPrices)
        //Log.i("paymenttt","payment From Home${lineItems.get(0).quantity},,,, ${orderPrices.get(0).subTotal}")
        paymentAddressFragment.arguments = bundle
        replaceFragment(paymentAddressFragment)
    }

    override fun goToPaymentFromAddress(selectedAddress: Addresse , lineItems: ArrayList<LineItem> ,orderPrices: ArrayList<OrderPrices>){
        myDetailsFlag=0
        val bundle=Bundle()
        val paymentFragment = PaymentFragment()
        bundle.putSerializable("selectedAddress", selectedAddress)
        bundle.putSerializable("lineItems",lineItems)
        bundle.putSerializable("orderPrice",orderPrices)
        paymentFragment.arguments=bundle
        replaceFragment(paymentFragment)

    }


    override fun goToOrderDetails(selectedOrder: Order) {
        val bundle=Bundle()
        bundle.putSerializable("selectedOrder",selectedOrder)
        val orderDetailsFragment=OrderDetailsFragment()
        orderDetailsFragment.arguments=bundle
        replaceFragment(orderDetailsFragment)
    }

    override fun goToOrderSummary(order: Order, totoalAmount: String, subTotal: String, taxAmount: String) {
        val bundle=Bundle()
        bundle.putSerializable("order",order)
        bundle.putString("totoalAmount",totoalAmount)
        bundle.putString("subTotal",subTotal)
        bundle.putString("taxAmount",taxAmount)
        val confirmOrderFragment= ConfirmOrderFragment()
        confirmOrderFragment.arguments=bundle
        replaceFragment(confirmOrderFragment)
        Log.i("TAG","order from activity $order")
    }


    override fun goToHome() {
        replaceFragment(homeFragment)
    }

    override fun goToProductDetails(id: Long) {
        myDetailsFlag=1
        val bundle=Bundle()
        val productInfo = ProductInfoFragment()
        bundle.putLong("productID",id)
        productInfo.arguments=bundle
        replaceFragment(productInfo)
    }

    private fun passMapDataToFragment() {
        myDetailsFlag=0
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