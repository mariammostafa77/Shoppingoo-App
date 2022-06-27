package com.example.mcommerce.me.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.mcommerce.AuthActivity
import com.example.mcommerce.R
import com.example.mcommerce.auth.login.view.LoginFormFragment
import com.example.mcommerce.me.view.setting.WithLoginAppSettingFragment
import com.example.mcommerce.me.view.setting.WithoutLoginAppSettingFragment
import com.example.mcommerce.network.CheckInternetConnectionFirstTime
import com.google.android.material.snackbar.Snackbar

class MeWithoutLoginFragment : Fragment() {

    lateinit var withoutLoginSettingICon : ImageView
    lateinit var btnGoToLogin:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_me_without_login, container, false)
        btnGoToLogin=view.findViewById(R.id.btnGoToLogin)
        withoutLoginSettingICon = view.findViewById(R.id.withoutLoginSettingICon)
        withoutLoginSettingICon.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, WithoutLoginAppSettingFragment())
            transaction.addToBackStack(null);
            transaction.commit()
        }
        btnGoToLogin.setOnClickListener {
            if(CheckInternetConnectionFirstTime.checkForInternet(requireContext())){
                requireActivity().finish()
                startActivity(Intent(requireContext(), AuthActivity::class.java))

            }else{
                var snake = Snackbar.make(view, "Check internet connection", Snackbar.LENGTH_LONG)
                snake.show()
            }

        }
        return view
    }

}