package com.example.mcommerce.auth.login.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.R
import com.example.mcommerce.auth.Register.view.RegisterFormFragment
import com.example.mcommerce.auth.login.viewModel.LoginViewModel
import com.example.mcommerce.auth.login.viewModel.LoginViewModelFactory
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient


class LoginFormFragment : Fragment() {
    lateinit var txtRegister:TextView
    lateinit var edtLoginEmail:EditText
    lateinit var edtLoginPassword:EditText
    lateinit var btnLogin:Button
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginViewModelFactory: LoginViewModelFactory
    var isSuccess:Boolean=false
    lateinit var loginEmail:String
    lateinit var loginPassword:String
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
            loginEmail=edtLoginEmail.text.toString()
            loginPassword=edtLoginPassword.text.toString()

            if (!loginEmail.isEmpty() && !loginPassword.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(loginEmail)
                    .matches() && loginPassword.length >= 6
            ) {
                loginViewModelFactory = LoginViewModelFactory(
                    Repository.getInstance(
                        AppClient.getInstance(),
                        requireContext()))
                loginViewModel =
                    ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
                loginViewModel.getCustomer()
                loginViewModel.customer.observe(viewLifecycleOwner) { customer ->
                    val editor =
                        requireContext().getSharedPreferences("userAuth", Context.MODE_PRIVATE)
                            .edit()

                    for (i in 0..customer.customers.size - 1) {
                        if (customer.customers[i].email == loginEmail && customer.customers[i].tags == loginPassword) {
                            isSuccess = true
                            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG)
                                .show()
                            Log.i("login",
                                "test " + customer.customers[i].tags.toString())
                            Log.i("login",
                                "login sussessfull: " + customer.customers[i].tags.toString())
                            editor.putString("email", customer.customers[i].email)
                            editor.putString("password", customer.customers[i].tags)
                            editor.putString("fname", customer.customers[i].first_name)
                            editor.putString("lname", customer.customers[i].last_name)
                            editor.putString("phone", customer.customers[i].phone)
                            editor.putString("cusomerID", customer.customers[i].id.toString())
                            editor.commit()
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            break
                        }
                    }
                    if (isSuccess == false) {
                        Toast.makeText(requireContext(),
                            "Email or Password not matched",
                            Toast.LENGTH_LONG).show()
                    }

                }
            }
            else{
                loginvalidatation()
            }
        }

        return view
    }

     fun loginvalidatation() {
        if (loginEmail.isEmpty()) {
            edtLoginEmail.setError("Email is required")
            edtLoginEmail.requestFocus()
        }
        if (loginPassword.isEmpty()) {
            edtLoginPassword.setError("Password is required")
            edtLoginPassword.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            edtLoginEmail.setError("please enter a valid email")
            edtLoginEmail.requestFocus()
        }
        if (loginPassword.length < 6) {
            edtLoginPassword.setError("min password length is 6")
            edtLoginPassword.requestFocus()
        }
    }

}