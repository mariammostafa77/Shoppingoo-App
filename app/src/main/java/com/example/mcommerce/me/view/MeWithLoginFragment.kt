package com.example.mcommerce.me.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.me.view.setting.AppSettingFragment
import com.example.mcommerce.orders.view.OrdersFragment
import com.example.mcommerce.shopping_cart.view.ShoppingCartFragment

class MeWithLogin : Fragment() {

    lateinit var settingICon: ImageView
    lateinit var shoppingCartIcon : ImageView
    lateinit var txtWelcomeUser : TextView
    lateinit var tvMoreOrders : TextView
    lateinit var ordersrecycler:RecyclerView

    var userName : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_me_with_login, container, false)
        initComponent(view)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val fname: String? = sharedPreferences.getString("fname","")
        val lname: String? = sharedPreferences.getString("lname","")


        tvMoreOrders.setOnClickListener(View.OnClickListener {
            replaceFragment(OrdersFragment())
        })

        txtWelcomeUser.append(" ${fname} ${lname}. ")
        settingICon.setOnClickListener {
            replaceFragment(AppSettingFragment())
        }
        shoppingCartIcon.setOnClickListener {
            replaceFragment(ShoppingCartFragment())
        }
        return view
    }

    private fun initComponent(view : View){
        settingICon = view.findViewById(R.id.settingICon)
        shoppingCartIcon = view.findViewById(R.id.shoppingCartIcon)
        txtWelcomeUser = view.findViewById(R.id.txtWelcomeUser)
        tvMoreOrders=view.findViewById(R.id.tvMoreOrders)
        ordersrecycler=view.findViewById(R.id.ordersrecycler)
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

}