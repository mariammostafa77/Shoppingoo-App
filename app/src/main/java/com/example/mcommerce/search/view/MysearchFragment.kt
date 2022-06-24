package com.example.mcommerce.search.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.AuthActivity
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.network.CheckInternetConnectionFirstTime
import com.example.mcommerce.network.InternetConnectionChecker
import com.example.mcommerce.search.viewModel.SearchViewModel
import com.example.mcommerce.search.viewModel.SearchViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_view.view.*
import java.util.*
import kotlin.collections.ArrayList


class MysearchFragment : Fragment() {
    lateinit var communicator: Communicator
    lateinit var productSearchRecyclerview:RecyclerView
    lateinit var productSearchAdapter: SearchAdapter
    lateinit var searchFactor:SearchViewModelFactory
    lateinit var searchViewModel:SearchViewModel
    lateinit var noDataSearchImg:ImageView
    lateinit var noDataSearchtxt:TextView
    lateinit var categoriesProductFactory: CategoriesViewFactory
    lateinit var categoriesProductViewModel: CategoriesViewModel
//lateinit var linearLayoutManager:LinearLayoutManager
    lateinit var edtSearch:AutoCompleteTextView
    lateinit var btnSearch:ImageView
    lateinit var output:String
    var productID:String=""
    var brandName:String=""
    var subCatName:String=""
    var allFavProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    var allVariantsID:ArrayList<Long> = ArrayList<Long>()
    var allProducts:List<Product> = ArrayList<Product>()
    lateinit var email:String
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    lateinit var allProductArrayList:ArrayList<Product>
    lateinit var filterProductArrayList:ArrayList<Product>
    var productsName:ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_mysearch, container, false)
        communicator = activity as Communicator
        noDataSearchImg=view.findViewById(R.id.noDataSearchImg)
        noDataSearchtxt=view.findViewById(R.id.txtNoSearchData)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
         email= sharedPreferences.getString("email","").toString()

        edtSearch=view.findViewById(R.id.edtSearch)
        btnSearch=view.findViewById(R.id.btnSearch)
        filterProductArrayList= ArrayList<Product>()

        productSearchRecyclerview=view.findViewById(R.id.searhProductRecyclerView)
        productSearchAdapter= SearchAdapter(communicator,filterProductArrayList,requireContext())
        productSearchRecyclerview.setAdapter(productSearchAdapter)

            searchFactor = SearchViewModelFactory(
                Repository.getInstance(AppClient.getInstance(), requireContext())
            )
           allProductArrayList = ArrayList<Product>()
            searchViewModel = ViewModelProvider(this, searchFactor).get(SearchViewModel::class.java)

            if(mySearchFlag==1) {
                if(CheckInternetConnectionFirstTime.checkForInternet(requireContext())) {
                    searchViewModel.getAllProducts()

                }else{
                   Toast.makeText(requireContext(),"Please check internet",Toast.LENGTH_LONG).show()
                }
                internetConnectionChecker = InternetConnectionChecker(requireContext())
                internetConnectionChecker.observe(this,{ isConnected ->
                    if (isConnected){
                        searchViewModel.getAllProducts()
                    }
                    else{
                        Toast.makeText(requireContext(),"Ops! You Lost internet connection!!!",Toast.LENGTH_LONG).show()


                    }
                })
                searchViewModel.onlineProducts.observe(viewLifecycleOwner) { product ->

                    allProductArrayList.clear()
                     allProducts=product
                    if (allProductArrayList.size == 0) {
                        allProductArrayList.addAll(product)

                    }
                    productsName.clear()
                    for(i in 0..product.size-1) {
                        product[i].title?.let { productsName.add(it) }
                    }
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,productsName)
                    edtSearch.threshold=1
                    edtSearch.setAdapter(adapter)
                    filterProductArrayList.clear()
                    edtSearch.setOnItemClickListener { adapterView, view, i, l ->
                        filterProductArrayList.clear()
                        var productName:String= adapter.getItem(i).toString()
                        if(productName.isNotEmpty()){

                            allProductArrayList.forEach{
                                if(it.title?.contains(productName) == true)
                                {
                                    filterProductArrayList.add(it)
                                }
                            }
                            if(filterProductArrayList.isEmpty()){
                                noDataSearchImg.visibility=View.VISIBLE
                                noDataSearchtxt.visibility=View.VISIBLE
                            }
                            else{
                                noDataSearchtxt.visibility=View.INVISIBLE
                                noDataSearchImg.visibility=View.INVISIBLE
                            }

                        }
                        else{
                            filterProductArrayList.clear()
                        }

                        productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)



                    }


                }

            }
        else if(mySearchFlag==2){

                output= arguments?.getString("catID").toString()
                productID= arguments?.getString("catID").toString()
                brandName= arguments?.getString("brandName").toString()
                subCatName= arguments?.getString("subCatName").toString()
                categoriesProductFactory = CategoriesViewFactory(
                    Repository.getInstance(
                        AppClient.getInstance(),
                        requireContext()))

                categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory).get(CategoriesViewModel::class.java)

                if(CheckInternetConnectionFirstTime.checkForInternet(requireContext())) {

                    categoriesProductViewModel.getCategoriesProduct(brandName,subCatName,productID)

                }else{
                    var snake = Snackbar.make(view, "Please check internet", Snackbar.LENGTH_LONG)
                    snake.show()
                }
                internetConnectionChecker = InternetConnectionChecker(requireContext())
                internetConnectionChecker.observe(this,{ isConnected ->
                    if (isConnected){
                        categoriesProductViewModel.getCategoriesProduct(brandName,subCatName,productID)
                    }
                    else{
                        var snake = Snackbar.make(view, "Ops! You Lost internet connection!!!", Snackbar.LENGTH_LONG)
                        snake.show()
                    }
                })



                categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner)  {

                    Log.i("TAG","Count  ${it.size}")
                    allProductArrayList.clear()
                    allProductArrayList.addAll(it)
                    productsName.clear()
                    it.forEach {

                        productsName.add(it.title!!)
                    }
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,productsName)
                    edtSearch.threshold=1
                    edtSearch.setAdapter(adapter)
                    filterProductArrayList.clear()
                    edtSearch.setOnItemClickListener { adapterView, view, i, l ->
                        filterProductArrayList.clear()
                      //  Toast.makeText(requireContext(),"clicked"+ adapter.getItem(i).toString(),Toast.LENGTH_LONG).show()
                        var productName:String= adapter.getItem(i).toString()
                        if(productName.isNotEmpty()){

                            allProductArrayList.forEach{
                                if(it.title?.contains(productName) == true)
                                {
                                    filterProductArrayList.add(it)
                                }
                            }
                            if(filterProductArrayList.isEmpty()){
                                noDataSearchImg.visibility=View.VISIBLE
                                noDataSearchtxt.visibility=View.VISIBLE
                            }
                            else{
                                noDataSearchImg.visibility=View.INVISIBLE
                                noDataSearchtxt.visibility=View.INVISIBLE
                            }

                        }
                        else{
                            filterProductArrayList.clear()
                        }

                        productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)



                    }


                }



            }
        btnSearch.setOnClickListener{
            filterProductArrayList.clear()
            var productName:String=edtSearch.text.toString().toLowerCase(Locale.getDefault())


                if(productName.isNotEmpty()){

                    allProductArrayList.forEach{
                        if(it.title?.toLowerCase(Locale.getDefault())?.contains(productName) == true)
                        {
                            filterProductArrayList.add(it)
                        }
                    }
                    if(filterProductArrayList.isEmpty()){
                        noDataSearchImg.visibility=View.VISIBLE
                        noDataSearchtxt.visibility=View.VISIBLE
                    }
                    else{
                        noDataSearchImg.visibility=View.INVISIBLE
                        noDataSearchtxt.visibility=View.INVISIBLE
                    }

                }
                else{
                    filterProductArrayList.clear()
                }



            productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)


        }




        return view
    }

}