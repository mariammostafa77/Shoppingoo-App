package com.example.mcommerce.me.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.auth.Register.viewModel.RegisterViewModel
import com.example.mcommerce.auth.Register.viewModel.RegisterViewModelFactory
import com.example.mcommerce.auth.model.CustomerDetail
import com.example.mcommerce.home.view.AdsAdapter
import com.example.mcommerce.home.view.DiscountCodeAdapter
import com.example.mcommerce.me.view.setting.AppSettingFragment
import com.example.mcommerce.me.view.setting.CustomerAddressAdapter
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.shopping_cart.view.ShoppingCartFragment

class MeWithLogin : Fragment() {

    lateinit var settingICon: ImageView
    lateinit var shoppingCartIcon : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_me_with_login, container, false)
        initComponent(view)
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


    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

}