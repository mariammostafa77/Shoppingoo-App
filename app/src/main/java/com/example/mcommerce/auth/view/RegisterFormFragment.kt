
package com.example.mcommerce.auth.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.R


class RegisterFormFragment : Fragment() {
   lateinit var btnSkip:Button
    lateinit var txtLogin:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       var view= inflater.inflate(R.layout.fragment_register_form, container, false)
        btnSkip=view.findViewById(R.id.btnSkip)
        txtLogin=view.findViewById(R.id.txtHaveAcc)
        txtLogin.setOnClickListener {
            var navController: NavController = Navigation.findNavController(it)
            var navDir: NavDirections =RegisterFormFragmentDirections.actionMyRegisterFragmentToMyLoginFragment()
            navController.navigate(navDir)
        }
        btnSkip.setOnClickListener {
            startActivity(Intent(requireContext(),HomeActivity::class.java))
        }
        return view
    }


}