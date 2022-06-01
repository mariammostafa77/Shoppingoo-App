package com.example.mcommerce.categories.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.brandProducts.view.BrandProductsAdapter
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.google.android.material.tabs.TabLayout

class CategoryFragment : Fragment() {
    lateinit var brandProductsAdapter: BrandProductsAdapter
    lateinit var categoriesProductFactory: CategoriesViewFactory
    lateinit var categoriesProductViewModel: CategoriesViewModel
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var categoriesTabLayout: TabLayout


    var id:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_category, container, false)
        categoriesTabLayout = view.findViewById(R.id.categoryTabBar)

       /* if(arguments != null){
            //id= arguments?.getString("brandId").toString()
            if(id==""){}
        }*/
        categoryRecyclerView=view.findViewById(R.id.categoryRecyclerView)
        brandProductsAdapter= BrandProductsAdapter()
        categoryRecyclerView.setAdapter(brandProductsAdapter)
        Log.i("TAG","From bradProductsRecyclerView ${id}")

        val tab = categoriesTabLayout.getTabAt(0)
        tab!!.select()
        id="273053712523"

        categoriesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        id="273053712523"
                        categoriesProductViewModel.getAllProducts(id)
                        Log.i("TAG","from category ${tab.position}")
                        true
                    }
                    1 -> {
                        id="273053679755"
                        categoriesProductViewModel.getAllProducts(id)
                        Log.i("TAG","from category ${tab.position}")
                        true
                    }
                    2 -> {
                        id="273053745291"

                        categoriesProductViewModel.getAllProducts(id)

                        Log.i("TAG","from category ${tab.position}")
                        true
                    }
                    3 -> {
                        id="273053778059"
                        categoriesProductViewModel.getAllProducts(id)

                        Log.i("TAG","from category ${tab.position}")
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
        categoriesProductViewModel.getAllProducts(id)
        categoriesProductViewModel.onlineProducts.observe(viewLifecycleOwner) {
            brandProductsAdapter.setUpdatedData(it,requireContext())
            Log.i("TAG","Count  ${it.size}")
        }





        return view
    }


}