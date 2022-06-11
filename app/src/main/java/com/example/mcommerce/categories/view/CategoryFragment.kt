package com.example.mcommerce.categories.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.FieldPosition

class CategoryFragment : Fragment() ,OnSubCategoryClickInterface, CurrencyConvertor {
    private lateinit var brandProductsAdapter: BrandProductsAdapter
    private lateinit var subCategoriesAdapter: SubCategoriesAdapter
    private lateinit var categoriesProductFactory: CategoriesViewFactory
    private lateinit var categoriesProductViewModel: CategoriesViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoriesTabLayout: TabLayout
    private lateinit var searchIcon:ImageView
    private lateinit var filterImg:ImageView
    private lateinit var communicator:Communicator
    private lateinit var categoryBarTitle:TextView
    private lateinit var dialog : BottomSheetDialog
    private var id:String=""
    private var brandName:String=""
    private var subCategorySelected:String=""

    lateinit var customerViewModel: CustomerViewModel
    lateinit var customerViewModelFactory: CustomerViewModelFactory

     var userId = ""
    var toCurrency = ""
    var convertorResult: Double = 0.0
    var allProducts: ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_category, container, false)
        initComponents(view)
        subCategoriesAdapter= SubCategoriesAdapter()
        brandProductsAdapter= BrandProductsAdapter(this)
        categoryRecyclerView.adapter = brandProductsAdapter
        communicator = activity as Communicator
        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)
        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
      //  userId = sharedPreferences.getString("cusomerID","").toString()

        id=""
       //checkArgs()
        categoriesProductFactory = CategoriesViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory)[CategoriesViewModel::class.java]
        categoryBarTitle.text=brandName
        filterImg.setOnClickListener(View.OnClickListener {
            val view = layoutInflater.inflate(R.layout.custom_bottom_sheet, null)
            val subTypeRecycle:RecyclerView=view.findViewById(R.id.subTypeRecycle)
            subTypeRecycle.adapter = subCategoriesAdapter
            dialog.setContentView(view)
            dialog.show()
        })
        categoriesProductViewModel.getAllProducts(brandName,"","")
        categoriesProductViewModel.onlineProductsTypes.observe(viewLifecycleOwner) {
            getProductTypes(it)
        }
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner) {products ->
            allProducts.addAll(products)
            brandProductsAdapter.setUpdatedData(products,requireContext(),communicator)
        }
        categoriesProductViewModel.allOnlineProducts.observe(viewLifecycleOwner) {
            getProductTypes(it)
        }
        categoriesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               onTabSelectedListener(tab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        searchIcon.setOnClickListener {
            mySearchFlag=2
            //communicator.goToSearchWithID(id)
            communicator.goToSearchWithAllData(id,brandName,subCategorySelected)

        }
        return view
    }

    private fun checkArgs() {
        Log.i("TAG","Brand name $brandName")
        if(arguments != null){
            brandName=arguments?.getString("brandTitle").toString()
        }else{
            brandName=""
        }
    }

    override fun onStart() {
        super.onStart()
        checkArgs()
        id=""
        subCategorySelected=""
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
    }
    override fun onSubCategoryClick(type:String) {
        subCategorySelected=type
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        Toast.makeText(requireContext(),subCategorySelected,Toast.LENGTH_LONG).show()
        dialog.dismiss()
    }
    override fun onPriceConverter(position: Int) : String{
        /*
           toCurrency = SavedSetting.loadCurrency(context!!)
            if(toCurrency.isNullOrEmpty()){
                toCurrency = "EGP"
            }
            customerViewModel.getAmountAfterConversion(toCurrency)
            customerViewModel.onlineCurrencyChanged.observe(viewLifecycleOwner) { result ->
                convertorResult = result.result
            }
       // Log.i("Testttttttttt", (allProducts.get(position).variants.get(0).price.toDouble() * convertorResult).toString() +" " +toCurrency)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        val result = (allProducts.get(position).variants.get(0).price.toDouble() * convertorResult)
        val roundoff = df.format(result)
        return "${roundoff}  ${toCurrency}"
*/
        return ""
    }

    private fun initComponents(view:View){
        categoriesTabLayout = view.findViewById(R.id.categoryTabBar)
        categoryRecyclerView=view.findViewById(R.id.categoryRecyclerView)
        searchIcon=view.findViewById(R.id.search_icon)
        categoryBarTitle=view.findViewById(R.id.categoryBarTitle)
        filterImg=view.findViewById(R.id.filterImg)
        dialog = BottomSheetDialog(requireContext())

    }
    private fun getProductTypes(allProductsTypes:List<Product>){
        val typesSet= mutableSetOf<String>()
        if(allProductsTypes.isNotEmpty()){
            for(element in allProductsTypes){
                typesSet.add(element.product_type)
            }
        }
        subCategoriesAdapter.setUpdatedData(typesSet.toList(),requireContext(),this)
    }
    private fun onTabSelectedListener(tab: TabLayout.Tab){
        when (tab.position) {
            0 -> {
                id=""
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            1 -> {
                id="273053712523"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            2 -> {
                id="273053679755"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            3 -> {
                id="273053745291"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            4 -> {
                id="273053778059"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            else -> false

        }
    }
    private fun getSubTypes() {
        if (id.isNotEmpty()) {
            categoriesProductViewModel.getProductsType(id)
        }
        else{
            categoriesProductViewModel.getAllProducts(brandName,"","")
        }
    }
}