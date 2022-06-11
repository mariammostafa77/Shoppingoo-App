
package com.example.mcommerce.auth.Register.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.R
import com.example.mcommerce.auth.Register.viewModel.RegisterViewModel
import com.example.mcommerce.auth.Register.viewModel.RegisterViewModelFactory
import com.example.mcommerce.auth.model.*
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import kotlinx.android.synthetic.main.fragment_register_form.*


class RegisterFormFragment : Fragment() {
   lateinit var btnSkip:Button
   lateinit var txtLogin:TextView
   lateinit var edtFName:EditText
   lateinit var edtLName:EditText
   lateinit var edtEmail:EditText
   lateinit var edtPassword:EditText
   lateinit var edtConfirmPASS:EditText
    lateinit var myEdtPhone:EditText
   lateinit var btnRegister:Button
   lateinit var registerViewModel:RegisterViewModel
   lateinit var registerViewModelFactory:RegisterViewModelFactory
   var sharedPreferences: SharedPreferences? = null
    lateinit var registerFName:String
    lateinit var registerLName:String
    lateinit var registerEmail:String
    lateinit var registerPassword:String
    lateinit var registerConfirmPassword:String
    lateinit var registerPhone:String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       var view= inflater.inflate(R.layout.fragment_register_form, container, false)

        btnSkip=view.findViewById(R.id.btnSkip)
        txtLogin=view.findViewById(R.id.txtHaveAcc)
        edtFName=view.findViewById(R.id.edtFName)
        edtLName=view.findViewById(R.id.edtLName)
        edtEmail=view.findViewById(R.id.edtRegisterEmail)
        edtPassword=view.findViewById(R.id.edtRegisterPassword)
        edtConfirmPASS=view.findViewById(R.id.edtConfirmPass)
        myEdtPhone=view.findViewById(R.id.edtPhone)
        btnRegister=view.findViewById(R.id.btnRegister)
        registerViewModelFactory = RegisterViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        registerViewModel = ViewModelProvider(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        txtLogin.setOnClickListener {

            var navController: NavController = Navigation.findNavController(it)
            var navDir: NavDirections =RegisterFormFragmentDirections.actionMyRegisterFragmentToMyLoginFragment()
            navController.navigate(navDir)
        }
        btnSkip.setOnClickListener {
            startActivity(Intent(requireContext(),HomeActivity::class.java))
        }


        btnRegister.setOnClickListener {
            registerFName = edtFName.text.toString()
            registerLName = edtLName.text.toString()
            registerEmail = edtEmail.text.toString()
            registerPassword = edtPassword.text.toString()
            registerConfirmPassword = edtConfirmPASS.text.toString()
            registerPhone = myEdtPhone.text.toString()
            if (!registerFName.isEmpty() && !registerLName.isEmpty() && !registerPhone.isEmpty() && !registerEmail.isEmpty() &&
                !registerPassword.isEmpty() && !registerConfirmPassword.isEmpty() && registerConfirmPassword == registerPassword && registerPassword.length >= 6 &&
                Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()
            ) {



            var customer = CustomerX()
            customer.first_name = registerFName
            customer.last_name = registerLName
            customer.email = registerEmail
            // customer.password = edtPassword.text.toString()
            // customer.password_confirmation = edtConfirmPASS.text.toString()
            customer.verified_email = true
            var phoneNumber: String = registerPhone
            customer.phone = phoneNumber

            //  customer.phone="01009843245"
            customer.tags = registerPassword
            customer.addresses = listOf(Addresse(address1 = "Alkafal",
                phone = "01203574583",
                city = "Alex",
                province = "",
                zip = "21552",
                last_name = "Lastnameson",
                first_name = "Mother",
                country = "CA"))

            var customDetai = CustomerDetail(customer)
            Toast.makeText(requireContext(), "" + myEdtPhone.text.toString(), Toast.LENGTH_LONG)
                .show()
            registerViewModel.postCustomer(customDetai)
            registerViewModel.customer.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(),
                        "Register Successfull: " + response.code().toString(),
                        Toast.LENGTH_LONG).show()
                    Log.i("Reg", "messs from success: " + response.body().toString())
                    val editor =
                        requireContext().getSharedPreferences("userAuth", Context.MODE_PRIVATE)
                            .edit()
                    editor.putString("email", response.body()!!.customer!!.email)
                    editor.putString("password", response.body()!!.customer!!.tags)
                    editor.putString("fname", response.body()!!.customer!!.first_name)
                    editor.putString("lname", response.body()!!.customer!!.last_name)
                    editor.putString("phone", response.body()!!.customer!!.phone)
                    editor.putString("cusomerID", response.body()!!.customer!!.id.toString())
                    editor.putBoolean("isLogin", true)
                    editor.commit()
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                } else {
                    Log.i("Reg", "messs: " + response.code().toString())
                    Log.i("Reg", "err: " + response.errorBody())
                    Toast.makeText(requireContext(),
                        "Email or Phone already Register!!: " + response.code().toString(),
                        Toast.LENGTH_LONG).show()

                }
            }
        }
            else{
               registrationValidation()
            }
        }


        return view
    }
    fun registrationValidation(){
        if (TextUtils.isEmpty(registerFName)) {
            edtFName.setError("First Name is requried")
            edtFName.requestFocus()
        }
        if (TextUtils.isEmpty(registerLName)) {
            edtLName.setError("Last Name is requried")
            edtLName.requestFocus()
        }
        if (TextUtils.isEmpty(registerEmail)) {
            edtEmail.setError("Email is requried")
            edtEmail.requestFocus()
        }
        if (TextUtils.isEmpty(registerPassword)) {
            edtPassword.setError("Password is requried")
            edtPassword.requestFocus()
        }
        if (TextUtils.isEmpty(registerConfirmPassword)) {
            edtConfirmPASS.setError("Confirm Password is requried")
            edtConfirmPASS.requestFocus()
        }
        if (TextUtils.isEmpty(registerPhone)) {
            myEdtPhone.setError("Phone is requried")
            myEdtPhone.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()) {
            edtEmail.setError("please enter email in correct format")
            edtEmail.requestFocus()
        }
        if (registerPassword.length < 6) {
            edtPassword.setError("required more than 6")
            edtPassword.requestFocus()
        }
        if (registerConfirmPassword != registerPassword) {
            edtConfirmPASS.setError("must be matched")
            edtConfirmPASS.requestFocus()
        }

    }

}