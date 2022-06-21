package com.example.mcommerce.confirmOrder.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orderDetails.view.OrderItemsAdapter
import com.example.mcommerce.orderDetails.viewModel.OrderDetailsViewModel
import com.example.mcommerce.orderDetails.viewModel.OrderDetailsViewModelFactory
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.shopping_cart.view.ShoppingCartFragment
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModelFactory


class ConfirmOrderFragment : Fragment() {

    private lateinit var itemRecycleView:RecyclerView
    private lateinit var tvSubTotal:TextView
    private lateinit var tvFees:TextView
    private lateinit var tvTotal:TextView
    private lateinit var tvAddress:TextView
    private lateinit var tvPhoneNum:TextView
    private lateinit var tvPaymentMethod:TextView
    private lateinit var okBtn:Button

    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var orderDetailsViewModelFactory: OrderDetailsViewModelFactory
    private lateinit var orderDetailsViewModel: OrderDetailsViewModel
    private lateinit var communicator: Communicator

    lateinit var shoppingCartViewModelFactory : ShoppingCartViewModelFactory
    lateinit var shoppingCartViewModel: ShoppingCartViewModel

    lateinit var myOrder: Order
    var fees:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_confirm_order, container, false)
        initComponent(view)
        Log.i("TAG","args $arguments")
        orderItemsAdapter= OrderItemsAdapter()
        communicator = activity as Communicator
        itemRecycleView.adapter = orderItemsAdapter
        orderDetailsViewModelFactory = OrderDetailsViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        orderDetailsViewModel = ViewModelProvider(this, orderDetailsViewModelFactory)[OrderDetailsViewModel::class.java]
        if(arguments != null){
            myOrder=arguments?.getSerializable("order") as Order
            //fees= arguments?.getDouble("fees")!!
            tvAddress.text=
                "${myOrder.shipping_address?.address1} ${myOrder.shipping_address?.city}, ${myOrder.shipping_address?.country}"
            tvTotal.text= arguments?.getString("totoalAmount")!!
            tvSubTotal.text= arguments?.getString("subTotal")!!
            tvFees.text=arguments?.getString("taxAmount")!!
            tvPhoneNum.text=myOrder.shipping_address?.phone.toString()
            tvPaymentMethod.text=myOrder.processing_method
            myOrder.line_items?.let { orderItemsAdapter.setUpdatedData(it,requireContext(),communicator) }

        }
        shoppingCartViewModelFactory = ShoppingCartViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        shoppingCartViewModel = ViewModelProvider(this, shoppingCartViewModelFactory).get(ShoppingCartViewModel::class.java)
        for(product in ShoppingCartFragment.userShoppingCartProducts){
            shoppingCartViewModel.deleteSelectedProduct(product.draft_order?.id.toString())
        }

        okBtn.setOnClickListener {
            communicator.goToHome()
        }


        return view
    }

  private fun initComponent(view:View){
      itemRecycleView=view.findViewById(R.id.itemRecycleView)
      tvSubTotal=view.findViewById(R.id.tvSubTotal)
      tvFees=view.findViewById(R.id.tvFees)
      tvTotal=view.findViewById(R.id.tvTotal)
      tvAddress=view.findViewById(R.id.tvAddress)
      tvPhoneNum=view.findViewById(R.id.tvPhoneNum)
      tvPaymentMethod=view.findViewById(R.id.tvPaymentMethod)
      okBtn=view.findViewById(R.id.okBtn)
    }
}