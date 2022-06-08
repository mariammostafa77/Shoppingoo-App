package com.example.mcommerce.categories.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.brandProducts.view.BrandProductsAdapter
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.model.Variants
import com.example.mcommerce.network.AppClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class CategoryFragment : Fragment() {
    lateinit var brandProductsAdapter: BrandProductsAdapter
    lateinit var categoriesProductFactory: CategoriesViewFactory
    lateinit var categoriesProductViewModel: CategoriesViewModel
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var categoriesTabLayout: TabLayout
    lateinit var searchIcon:ImageView
    lateinit var allCatProductArrayList:ArrayList<Product>
    lateinit var accessoriesArrayList:ArrayList<Product>
    lateinit var communicator:Communicator
    lateinit var allVariant:ArrayList<Variants>
    lateinit var openOptionsBtn:FloatingActionButton
    lateinit var shoesBtn:FloatingActionButton
    lateinit var tshirtBtn:FloatingActionButton
    lateinit var accessoriesBtn:FloatingActionButton
    lateinit var categoryBarTitle:TextView
    private val openFabAnim:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_fab) }
    private val closeFabAnim:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_fab) }
    private val toBottomFabAnim:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private val fromBottomAnim:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private var fabClicked : Boolean = false
    private var id:String=""
    private var brandName:String=""
    private var subCategorySelected:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_category, container, false)
        init(view)
        allCatProductArrayList= ArrayList<Product>()
        accessoriesArrayList=ArrayList<Product>()
        allVariant= ArrayList<Variants>()
        communicator = activity as Communicator
        if(arguments != null){
            brandName=arguments?.getString("brandTitle").toString()
            Log.i("TAG","from category Fragment $brandName")
        }else{
            brandName=""
        }
        categoryBarTitle.text=brandName
        Log.i("TAG","from category oncreate $brandName")
        Toast.makeText(requireContext(),"brand name $brandName",Toast.LENGTH_LONG).show()

        openOptionsBtn.setOnClickListener(View.OnClickListener {
            onOpenOptionsBtnClick()
        })

        accessoriesBtn.setOnClickListener(View.OnClickListener {
            onAccessoriesBtnClick()
            Log.i("TAG","shoes")
        })

        shoesBtn.setOnClickListener(View.OnClickListener {
            onShoesBtnClick()
            Log.i("TAG","shoes")
        })

        tshirtBtn.setOnClickListener(View.OnClickListener {
            onTshirtBtnClick()
        })


        brandProductsAdapter= BrandProductsAdapter()
        categoryRecyclerView.setAdapter(brandProductsAdapter)
        //Log.i("TAG","From bradProductsRecyclerView ${id}")

        val tab = categoriesTabLayout.getTabAt(0)
        tab!!.select()
        id=""

        categoriesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        id=""
                        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
                        true
                    }
                    1 -> {
                        id="273053712523"
                        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
                        true
                    }
                    2 -> {
                        id="273053679755"
                        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
                        true
                    }
                    3 -> {
                        id="273053745291"
                        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
                        true
                    }
                    4 -> {
                        id="273053778059"
                        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
                        true
                    }
                    else -> false

                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        categoriesProductFactory = CategoriesViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory).get(CategoriesViewModel::class.java)

        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner) {products ->
            allCatProductArrayList.addAll(products)
            brandProductsAdapter.setUpdatedData(products,requireContext(),communicator)


        }


        //categoriesProductViewModel.onlineProducts.observe(viewLifecycleOwner){}
        searchIcon.setOnClickListener {
            mySearchFlag=2
            //communicator.goToSearchWithID(id)
            communicator.goToSearchWithAllData(id,brandName,subCategorySelected)

        }


        //Log.i("listSize","size"+allCatProductArrayList.size.toString())


        return view
    }

    override fun onStart() {
        super.onStart()
        fabClicked=false
        id=""
        subCategorySelected=""
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        //categoryBarTitle.text=brandName

    }

    override fun onStop() {
        super.onStop()
        //brandName=""

    }
    private fun init(view:View){
        categoriesTabLayout = view.findViewById(R.id.categoryTabBar)
        categoryRecyclerView=view.findViewById(R.id.categoryRecyclerView)
        searchIcon=view.findViewById(R.id.search_icon)
        openOptionsBtn=view.findViewById(R.id.openOptionsBtn)
        accessoriesBtn=view.findViewById(R.id.accessoriesBtn)
        shoesBtn=view.findViewById(R.id.shoesBtn)
        tshirtBtn=view.findViewById(R.id.tshirtBtn)
        categoryBarTitle=view.findViewById(R.id.categoryBarTitle)
    }

    private fun onOpenOptionsBtnClick(){
        if(!fabClicked){
           openSubCategoriesBtn()
        }
        else{
            clossSubCategoriesBtn()

        }
    }
    private fun onAccessoriesBtnClick(){
        subCategorySelected="Accessories"
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        Toast.makeText(requireContext(),"Accessories checked",Toast.LENGTH_LONG).show()
        clossSubCategoriesBtn()
    }
    private fun onShoesBtnClick(){
        subCategorySelected="SHOES"
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        Toast.makeText(requireContext(),"Shoes checked",Toast.LENGTH_LONG).show()
        clossSubCategoriesBtn()
    }
    private fun onTshirtBtnClick(){
        subCategorySelected="T-SHIRTS"
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        Toast.makeText(requireContext(),"TShirt checked",Toast.LENGTH_LONG).show()
        clossSubCategoriesBtn()
    }

    private fun clossSubCategoriesBtn(){
        openOptionsBtn.startAnimation(closeFabAnim)
        accessoriesBtn.startAnimation(toBottomFabAnim)
        shoesBtn.startAnimation(toBottomFabAnim)
        tshirtBtn.startAnimation(toBottomFabAnim)
        accessoriesBtn.visibility = INVISIBLE
        shoesBtn.visibility = INVISIBLE
        tshirtBtn.visibility = INVISIBLE
        fabClicked = false
    }
    private fun openSubCategoriesBtn(){
        openOptionsBtn.startAnimation(openFabAnim)
        accessoriesBtn.startAnimation(fromBottomAnim)
        shoesBtn.startAnimation(fromBottomAnim)
        tshirtBtn.startAnimation(fromBottomAnim)
        accessoriesBtn.visibility = VISIBLE
        shoesBtn.visibility = VISIBLE
        tshirtBtn.visibility = VISIBLE
        fabClicked = true
    }
}