package com.example.mcommerce.orderDetails.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orderDetails.viewModel.OrderDetailsViewModel
import com.example.mcommerce.orderDetails.viewModel.OrderDetailsViewModelFactory
import com.example.mcommerce.orders.model.Order

class OrderDetailsFragment : Fragment() {
    private lateinit var tvOrderCreatedAt:TextView
    private lateinit var tvOrderAddress:TextView
    private lateinit var tvOrderTotalPrice:TextView
    private lateinit var orderItemsRecycle:RecyclerView
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var orderDetailsViewModelFactory: OrderDetailsViewModelFactory
    private lateinit var orderDetailsViewModel: OrderDetailsViewModel
    private lateinit var communicator:Communicator

    lateinit var selectedOrder: Order


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_order_details, container, false)
        initComponent(view)
        orderItemsAdapter= OrderItemsAdapter()
        communicator = activity as Communicator
        orderItemsRecycle.adapter = orderItemsAdapter
        orderDetailsViewModelFactory = OrderDetailsViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        orderDetailsViewModel = ViewModelProvider(this, orderDetailsViewModelFactory)[OrderDetailsViewModel::class.java]

        if(arguments != null){
            selectedOrder=arguments?.getSerializable("selectedOrder") as Order
        }
        tvOrderAddress.text=
            "${selectedOrder.customer?.default_address?.address1},${selectedOrder.customer?.default_address?.city}"
        tvOrderCreatedAt.text=selectedOrder.created_at
        tvOrderTotalPrice.text=
            "${selectedOrder.current_total_price} ${selectedOrder.currency}"

        orderItemsAdapter.setUpdatedData(selectedOrder.line_items!!,requireContext(),communicator)

        return view
    }
    fun initComponent(view:View){
        tvOrderAddress=view.findViewById(R.id.tvOrderAddress)
        tvOrderCreatedAt=view.findViewById(R.id.tvOrderCreatedAt)
        orderItemsRecycle=view.findViewById(R.id.orderItemsRecycle)
        tvOrderTotalPrice=view.findViewById(R.id.tvOrderTotalPrice)
    }
}