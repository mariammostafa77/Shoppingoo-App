package com.example.mcommerce.auth.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.mcommerce.R


class LoginFormFragment : Fragment() {
    lateinit var txtRegister:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       var view=inflater.inflate(R.layout.fragment_login_form, container, false)
        txtRegister=view.findViewById(R.id.txtDonnotHaveAcc)
        txtRegister.setOnClickListener {
            var navController: NavController = Navigation.findNavController(it)
            var navDir: NavDirections =LoginFormFragmentDirections.actionMyLoginFragmentToMyRegisterFragment()
            navController.navigate(navDir)
        }
        return view
    }


}