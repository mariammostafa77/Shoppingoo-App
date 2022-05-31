package com.example.mcommerce.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.network.RetrofitHelper
import com.example.mcommerce.network.ServiceApi
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.home.viewModel.HomeViewModelFactory
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.BrandsClient
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    lateinit var bradsRecyclerView: RecyclerView
    lateinit var brandAdapter: BrandAdapter
    lateinit var homeFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var linearLayoutManager: LinearLayoutManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view:View = inflater.inflate(R.layout.fragment_home, container, false)

        bradsRecyclerView=view.findViewById(R.id.bradsRecyclerView)
        linearLayoutManager=LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        bradsRecyclerView.setLayoutManager(linearLayoutManager)
        brandAdapter= BrandAdapter()
        bradsRecyclerView.setAdapter(brandAdapter)
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                BrandsClient.getInstance(""),
                requireContext()))
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

        homeViewModel.getAllProducts()
        homeViewModel.onlineBrands.observe(viewLifecycleOwner) { movies ->
            Log.i("TAG","hello from home fragment ${homeViewModel.onlineBrands.value?.get(1)?.id}")
            homeViewModel.onlineBrands.value?.let { brandAdapter.setUpdatedData(it,requireContext()) }

        }


        return view
    }


}