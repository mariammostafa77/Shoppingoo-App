package com.example.mcommerce.auth.login.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.auth.Register.view.RegisterFormFragment
import com.example.mcommerce.auth.login.viewModel.LoginViewModel
import com.example.mcommerce.auth.login.viewModel.LoginViewModelFactory
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.view.MysearchFragment


class LoginFormFragment : Fragment() {
    lateinit var txtRegister:TextView
    lateinit var edtLoginEmail:EditText
    lateinit var edtLoginPassword:EditText
    lateinit var btnLogin:Button
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginViewModelFactory: LoginViewModelFactory
    var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       var view=inflater.inflate(R.layout.fragment_login_form, container, false)
        txtRegister=view.findViewById(R.id.txtDonnotHaveAcc)
        edtLoginEmail=view.findViewById(R.id.edtLoginEmail)
        edtLoginPassword=view.findViewById(R.id.edtLoginPassword)
        btnLogin=view.findViewById(R.id.btnLogin)
        txtRegister.setOnClickListener {
//            var navController: NavController = Navigation.findNavController(it)
//            var navDir: NavDirections =LoginFormFragmentDirections.actionMyLoginFragmentToMyRegisterFragment()
//            navController.navigate(navDir)
            val fragment: Fragment = RegisterFormFragment()
            val fragmentManager: FragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(com.example.mcommerce.R.id.frameLayout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        btnLogin.setOnClickListener {
            var myEmail:String=edtLoginEmail.text.toString()
            var myPassword:String=edtLoginPassword.text.toString()
            loginViewModelFactory = LoginViewModelFactory(
                Repository.getInstance(
                    AppClient.getInstance(),
                    requireContext()))
            loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
            loginViewModel.getCustomer()
            loginViewModel.customer.observe(viewLifecycleOwner) { customer ->
                val editor = requireContext().getSharedPreferences("userAuth", Context.MODE_PRIVATE).edit()

                for(i in 0..customer.customers.size-1) {
                    if (customer.customers[i].email==myEmail &&customer.customers[i].tags==myPassword) {


                        Log.i("login", "login sussessfull: " + customer.customers[i].tags.toString())
                        editor.putString("email", customer.customers[i].email)
                        editor.putString("password",customer.customers[i].tags)
                        editor.putString("fname", customer.customers[i].first_name)
                        editor.putString("lname", customer.customers[i].last_name)
                        editor.putString("phone", customer.customers[i].phone)
                        editor.putString("cusomerID", customer.customers[i].id.toString())
                        editor.putBoolean("isLogin", true)
                        editor.commit()
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        break
                    } else {
                        Log.i("login", "login failed: " + "null")
                    }
                }

            }
        }

        return view
    }


}