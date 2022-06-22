package com.example.mcommerce.categories.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.favourite.view.FavouriteFragment
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.viewModel.SearchViewModel
import com.example.mcommerce.search.viewModel.SearchViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.FieldPosition

class CategoryFragment(var flag:Int) : Fragment() ,OnSubCategoryClickInterface {
    private lateinit var brandProductsAdapter: BrandProductsAdapter
    private lateinit var subCategoriesAdapter: SubCategoriesAdapter
    private lateinit var categoriesProductFactory: CategoriesViewFactory
    private lateinit var categoriesProductViewModel: CategoriesViewModel
    private lateinit var noInternetCategoryLayout:ConstraintLayout
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoriesTabLayout: TabLayout
    private lateinit var searchIcon:ImageView
    private lateinit var favorite_icon:ImageView
    private lateinit var filterImg:ImageView
    private lateinit var imgNoData:ImageView
    private lateinit var tvNoData:TextView
    private lateinit var applayBtn:Button
    private lateinit var communicator:Communicator
    private lateinit var categoryBarTitle:TextView
    private lateinit var dialog : BottomSheetDialog
    private lateinit var priceSlider:RangeSlider
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var customerViewModelFactory: CustomerViewModelFactory
    lateinit var searchFactor: SearchViewModelFactory
    lateinit var searchViewModel: SearchViewModel
    private var  collectionId:String=""
    private var brandName:String=""
    private var subCategorySelected:String=""
    private var maxPrice: Double=0.0
    private var priceSliderPrice: Double=0.0
    private var priceSelectesConverted:Double=0.0
    private var allVariantsID:ArrayList<Long> = ArrayList<Long>()
    private var allFavProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    private var allProducts: ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_category, container, false)
        initComponents(view)
        if(flag == 0){
            checkArgs()
        } else{
            brandName=""
        }
        searchFactor = SearchViewModelFactory(
            Repository.getInstance(AppClient.getInstance(), requireContext())
        )
        Log.i("TAG","")
        searchViewModel = ViewModelProvider(this, searchFactor).get(SearchViewModel::class.java)
        subCategoriesAdapter= SubCategoriesAdapter()
        brandProductsAdapter= BrandProductsAdapter()
        categoryRecyclerView.adapter = brandProductsAdapter
        communicator = activity as Communicator
        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)
        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")
        collectionId=""
        categoriesProductFactory = CategoriesViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory)[CategoriesViewModel::class.java]
        categoryBarTitle.text=brandName
        filterImg.setOnClickListener(View.OnClickListener {
            val view = layoutInflater.inflate(R.layout.custom_bottom_sheet, null)
            val subTypeRecycle:RecyclerView=view.findViewById(R.id.subTypeRecycle)
            applayBtn=view.findViewById(R.id.applayBtn)
            priceSlider=view.findViewById(R.id.priceSlider)
            priceSlider.valueFrom = 0.0F
            val strPrice =  SavedSetting.getPrice(maxPrice.toString(), requireContext())
            val delim = " "
            val list = strPrice.split(delim)
            priceSlider.valueTo =list[0].toFloat()
            priceSlider.addOnChangeListener { rangeSlider, value, fromUser ->
                priceSliderPrice= value.toDouble()
            }
            applayBtn.setOnClickListener {
                if(priceSliderPrice != 0.0){
                    if(list[1] != "EGP"){
                        categoriesProductViewModel.getAmountAfterConversionInEgp(list[1])
                    }
                    else{
                        filterProductsByPrice(allProducts,priceSliderPrice)
                    }
                }
                dialog.dismiss()
            }
            subTypeRecycle.adapter = subCategoriesAdapter
            priceSlider=view.findViewById(R.id.priceSlider)
            dialog.setContentView(view)
            dialog.show()
        })
        categoriesProductViewModel.onlineCurrencyChangedInEgp.observe(viewLifecycleOwner){
            priceSelectesConverted=priceSliderPrice/it.result
            filterProductsByPrice(allProducts,priceSelectesConverted)
        }

        categoriesProductViewModel.getCategoriesProduct(brandName,subCategorySelected, collectionId)
        getSubTypes()
        categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner) {products ->
            allProducts.clear()
            allProducts.addAll(products)
            brandProductsAdapter.setUpdatedData(products,requireContext(),communicator)
            for(product in products){
                if(product.variants?.get(0)?.price?.toDouble()!! > maxPrice){
                    maxPrice = product.variants?.get(0)?.price?.toDouble()!!
                }
            }
            checkEmptyArray(products)
        }
        categoriesProductViewModel.allOnlineProductsSubTypes.observe(viewLifecycleOwner) {
            getProductTypes(it)
            Log.i("TAG","")
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
            communicator.goToSearchWithAllData( collectionId,brandName,subCategorySelected)

        }

        return view
    }

    private fun checkArgs() {
        if(arguments != null){
            brandName=arguments?.getString("brandTitle").toString()
            Log.i("TAG","Brand name from category $brandName")
        }else{
            brandName=""
        }
    }

    override fun onStart() {
        super.onStart()
        collectionId=""
        subCategorySelected=""
    }
    override fun onSubCategoryClick(type:String) {
        subCategorySelected=type
        categoriesProductViewModel.getCategoriesProduct(brandName,subCategorySelected, collectionId)
        Toast.makeText(requireContext(),subCategorySelected,Toast.LENGTH_LONG).show()
        //dialog.dismiss()
    }



    private fun initComponents(view:View){
        categoriesTabLayout = view.findViewById(R.id.categoryTabBar)
        categoryRecyclerView=view.findViewById(R.id.categoryRecyclerView)
        searchIcon=view.findViewById(R.id.search_icon)
        categoryBarTitle=view.findViewById(R.id.categoryBarTitle)
        filterImg=view.findViewById(R.id.filterImg)
        favorite_icon=view.findViewById(R.id.favorite_icon)
        dialog = BottomSheetDialog(requireContext())
        tvNoData = view.findViewById(R.id.tvNoData)
        imgNoData=view.findViewById(R.id.imgNoData)
        noInternetCategoryLayout=view.findViewById(R.id.noInternetCategoryLayout)

    }
    private fun getProductTypes(allProductsTypes:List<Product>){
        val typesSet= mutableSetOf<String>()
        if(allProductsTypes.isNotEmpty()){
            for(element in allProductsTypes){
                element.product_type?.let { typesSet.add(it) }
            }
        }
        subCategoriesAdapter.setUpdatedData(typesSet.toList(),requireContext(),this)
    }
    private fun onTabSelectedListener(tab: TabLayout.Tab){
        when (tab.position) {
            0 -> {
                collectionId=""
                maxPrice=0.0
                categoriesProductViewModel.getCategoriesProduct(brandName,"", collectionId)
                getSubTypes()
                true
            }
            1 -> {
                collectionId="273053712523"
                maxPrice=0.0
                categoriesProductViewModel.getCategoriesProduct(brandName,"", collectionId)
                getSubTypes()
                true
            }
            2 -> {
                collectionId="273053679755"
                maxPrice=0.0
                categoriesProductViewModel.getCategoriesProduct(brandName,"", collectionId)
                getSubTypes()
                true
            }
            3 -> {
                collectionId="273053745291"
                maxPrice=0.0
                categoriesProductViewModel.getCategoriesProduct(brandName,"", collectionId)
                getSubTypes()
                true
            }
            4 -> {
                collectionId="273053778059"
                maxPrice=0.0
                categoriesProductViewModel.getCategoriesProduct(brandName,"", collectionId)
                getSubTypes()
                true
            }
            else -> false

        }
    }
    private fun getSubTypes() {
        categoriesProductViewModel.getSubType(brandName,"",collectionId)
    }
    private fun checkEmptyArray(products:List<Product>){
        if(products.isEmpty()){
            tvNoData.visibility=View.VISIBLE
            imgNoData.visibility=View.VISIBLE
            filterImg.visibility=View.INVISIBLE

        }else{
            tvNoData.visibility=View.INVISIBLE
            imgNoData.visibility=View.INVISIBLE
            filterImg.visibility=View.VISIBLE
        }
    }
    private fun filterProductsByPrice(productsList: List<Product>,priceSliderPrice:Double){
        var filteredProduct:ArrayList<Product> = ArrayList<Product>()
        filteredProduct.clear()
        for (product in productsList){
            if(product.variants?.get(0)?.price?.toDouble()!! <= priceSliderPrice){
                filteredProduct.add(product)
            }
        }
        Log.i("","")
        brandProductsAdapter.setUpdatedData(filteredProduct,requireContext(),communicator)
        checkEmptyArray(filteredProduct)
    }

}