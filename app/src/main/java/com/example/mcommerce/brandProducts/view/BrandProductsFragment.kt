package com.example.mcommerce.brandProducts.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.brandProducts.viewModel.BrandProductsViewFactory
import com.example.mcommerce.brandProducts.viewModel.BrandProductsViewModel
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient

class BrandProductsFragment : Fragment() {


    lateinit var bradProductsRecyclerView: RecyclerView
    lateinit var brandProductsAdapter: BrandProductsAdapter
    lateinit var brandProductsFactory: BrandProductsViewFactory
    lateinit var brandProductsViewModel: BrandProductsViewModel
    var id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View=inflater.inflate(R.layout.fragment_brand_products, container, false)

       if(arguments != null){
           id= arguments?.getString("brandId").toString()
       }

        bradProductsRecyclerView=view.findViewById(R.id.brandProductsRecycleView)
        brandProductsAdapter= BrandProductsAdapter()
        bradProductsRecyclerView.setAdapter(brandProductsAdapter)
        Log.i("TAG","From bradProductsRecyclerView ${id}")
        brandProductsFactory = BrandProductsViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        brandProductsViewModel = ViewModelProvider(this, brandProductsFactory).get(BrandProductsViewModel::class.java)

        brandProductsViewModel.getAllProducts(id)
        brandProductsViewModel.onlineBrandProducts.observe(viewLifecycleOwner) { products ->
            brandProductsViewModel.onlineBrandProducts.value?.let { brandProductsAdapter.setUpdatedData(it,requireContext()) }

        }

        return view
    }

}