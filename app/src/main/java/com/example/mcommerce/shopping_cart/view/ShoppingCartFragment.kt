package com.example.mcommerce.shopping_cart.view

import android.content.Context
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.view.SearchAdapter
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModelFactory


class ShoppingCartFragment : Fragment() {

    lateinit var txtSubTotal : TextView
    lateinit var btnProceedToCheckout: Button
    lateinit var shoppingCartRecyclerView: RecyclerView
    lateinit var shoppingCartAdapter: ShoppingCartAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var shoppingCartViewModelFactory : ShoppingCartViewModelFactory
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    var userShoppingCartProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()

    var subTotal : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        initComponent(view)
        shoppingCartRecyclerView.setLayoutManager(linearLayoutManager)
        shoppingCartRecyclerView.setAdapter(shoppingCartAdapter)

        shoppingCartViewModelFactory = ShoppingCartViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        shoppingCartViewModel = ViewModelProvider(this, shoppingCartViewModelFactory).get(ShoppingCartViewModel::class.java)

        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")
        val note = "card"
        shoppingCartViewModel.getShoppingCardProducts()
        shoppingCartViewModel.onlineShoppingCartProduct.observe(viewLifecycleOwner) { cartProducts ->
           for (i in 0..cartProducts.size-1){
                if(cartProducts.get(i).note == note && cartProducts.get(i).email == email){
                    Log.i("TTTTTTTTT", "ok: note =  ${cartProducts.get(i).note} \n email : ${cartProducts.get(i).email}, shared email : ${email} ")
                    userShoppingCartProducts.add(cartProducts.get(i))
                }
            }
            shoppingCartAdapter.setUserShoppingCartProducts(requireContext(),userShoppingCartProducts)
            for (i in 0..userShoppingCartProducts.size-1){
                var price = (userShoppingCartProducts[i].line_items?.get(0)!!.price)?.toDouble()
                if (price != null) {
                    subTotal += price
                }
            }
            txtSubTotal.text = subTotal.toString()
        }

        return view
    }


    private fun initComponent(view: View){
        txtSubTotal = view.findViewById(R.id.txtSubTotal)
        shoppingCartRecyclerView = view.findViewById(R.id.shoppingCartRecyclerView)
        shoppingCartAdapter= ShoppingCartAdapter()
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        btnProceedToCheckout = view.findViewById(R.id.btnProceedToCheckout)
    }



}