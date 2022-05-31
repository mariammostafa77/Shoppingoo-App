package com.example.mcommerce.search.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.BrandsClient
import com.example.mcommerce.search.viewModel.SearchViewModel
import com.example.mcommerce.search.viewModel.SearchViewModelFactory
import java.util.*
import kotlin.collections.ArrayList


class MysearchFragment : Fragment() {
lateinit var communicator: Communicator
lateinit var productSearchRecyclerview:RecyclerView
lateinit var productSearchAdapter: SearchAdapter
lateinit var searchFactor:SearchViewModelFactory
lateinit var searchViewModel:SearchViewModel
lateinit var linearLayoutManager:LinearLayoutManager
lateinit var edtSearch:EditText
lateinit var btnSearch:Button
    lateinit var allProductArrayList:ArrayList<Product>
    lateinit var filterProductArrayList:ArrayList<Product>
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


        edtSearch=view.findViewById(R.id.edtSearch)
        btnSearch=view.findViewById(R.id.btnSearch)
        filterProductArrayList= ArrayList<Product>()

        productSearchRecyclerview=view.findViewById(R.id.searhProductRecyclerView)
        linearLayoutManager= LinearLayoutManager(requireContext())
        productSearchRecyclerview.setLayoutManager(linearLayoutManager)
        productSearchAdapter= SearchAdapter(communicator,filterProductArrayList,requireContext())
        productSearchRecyclerview.setAdapter(productSearchAdapter)
        searchFactor= SearchViewModelFactory(
            Repository.getInstance(BrandsClient.getInstance(),requireContext())
        )
        searchViewModel=ViewModelProvider(this,searchFactor).get(SearchViewModel::class.java)
        searchViewModel.getAllProducts()

        searchViewModel.onlineProducts.observe(viewLifecycleOwner){product ->

            allProductArrayList= ArrayList<Product>()
            if(allProductArrayList.size==0){
            allProductArrayList.addAll(product)
                }

           // Log.i("filterPro","from product ${allProductArrayList.toString()}")

        }

        btnSearch.setOnClickListener{
            filterProductArrayList.clear()
            var productName:String=edtSearch.text.toString().toLowerCase(Locale.getDefault())
           // allProductArrayList.forEach{

                if(productName.isNotEmpty()){

                    allProductArrayList.forEach{
                        if(it.title.toLowerCase(Locale.getDefault()).contains(productName))
                        {
                            filterProductArrayList.add(it)
                        }
                    }

                }
                else{
                    filterProductArrayList.clear()
                }


          //  }
            productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)


        }
        return view
    }


}