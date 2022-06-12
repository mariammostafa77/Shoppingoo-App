package com.example.mcommerce.orders.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.viewModel.OrdersViewFactory
import com.example.mcommerce.orders.viewModel.OrdersViewModel

class OrdersFragment : Fragment(),OnOrderClickListenerInterface {

    private lateinit var ordersRecycleView:RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var ordersViewFactory: OrdersViewFactory
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var communicator: Communicator
    private var id:String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_orders, container, false)
        ordersRecycleView=view.findViewById(R.id.ordersRecycleView)
        communicator = activity as Communicator
        ordersAdapter=OrdersAdapter()
        ordersRecycleView.adapter=ordersAdapter
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        id = sharedPreferences.getString("cusomerID","").toString()
        ordersViewFactory = OrdersViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        ordersViewModel = ViewModelProvider(this, ordersViewFactory)[OrdersViewModel::class.java]
        if(id.isNotEmpty()){
            ordersViewModel.getAllOrders(id)
        }
        ordersViewModel.allOnlineOrders.observe(viewLifecycleOwner) {
            ordersAdapter.setUpdatedData(it,requireContext(),this)
        }
        return view
    }

    override fun onOrderClickListener(order: Order) {
        communicator.goToOrderDetails(order)
    }

}