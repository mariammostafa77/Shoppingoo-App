package com.example.mcommerce.orders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
class OrdersFragment : Fragment() {

    private lateinit var ordersRecycleView:RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_orders, container, false)
        ordersRecycleView=view.findViewById(R.id.ordersRecycleView)
        ordersAdapter=OrdersAdapter()
        ordersRecycleView.adapter=ordersAdapter

        return view
    }

}