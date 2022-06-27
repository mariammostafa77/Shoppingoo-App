package com.example.mcommerce.orderDetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orders.model.Order

class OrderDetailsFragment : Fragment() {
    private lateinit var tvOrderCreatedAt:TextView
    private lateinit var tvOrderAddress:TextView
    private lateinit var tvOrderTotalPrice:TextView
    private lateinit var orderItemsRecycle:RecyclerView
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var communicator:Communicator
    private lateinit var orderDetailsBackIcon:ImageView
    private lateinit var tvOrderId:TextView

    lateinit var selectedOrder: Order


    @SuppressLint("SetTextI18n")
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
        orderDetailsBackIcon.setOnClickListener {
            val manager: FragmentManager = activity!!.supportFragmentManager
            val trans: FragmentTransaction = manager.beginTransaction()
            trans.remove(this)
            trans.commit()
            manager.popBackStack()
        }

        if(arguments != null){
            selectedOrder=arguments?.getSerializable("selectedOrder") as Order
        }
        tvOrderAddress.text=
            "${selectedOrder.customer?.default_address?.address1},${selectedOrder.customer?.default_address?.city}"

        val strCreatedAt = selectedOrder.created_at
        val delim1 = "T"
        val list1 = strCreatedAt?.split(delim1)
        val delim2 = "+"
        val list2 = list1?.get(1)?.split(delim2)
        tvOrderCreatedAt.text="${list1?.get(0)}/${list2?.get(0)}"

        val amount = SavedSetting.getPrice(selectedOrder.current_total_price.toString(), requireContext())
        tvOrderTotalPrice.text = amount
        tvOrderId.text= selectedOrder.id.toString()
        orderItemsAdapter.setUpdatedData(selectedOrder.line_items!!,requireContext(),communicator)

        return view
    }
    fun initComponent(view:View){
        tvOrderAddress=view.findViewById(R.id.tvOrderAddress)
        tvOrderCreatedAt=view.findViewById(R.id.tvOrderCreatedAt)
        orderItemsRecycle=view.findViewById(R.id.orderItemsRecycle)
        tvOrderTotalPrice=view.findViewById(R.id.tvOrderTotalPrice)
        tvOrderId=view.findViewById(R.id.tvOrderId)
        orderDetailsBackIcon=view.findViewById(R.id.orderDetailsBackIcon)
    }

}