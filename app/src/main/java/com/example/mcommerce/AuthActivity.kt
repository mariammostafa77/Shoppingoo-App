package com.example.mcommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mcommerce.auth.Register.view.RegisterFormFragment
import com.example.mcommerce.auth.login.view.LoginFormFragment
import com.example.mcommerce.me.viewmodel.SavedSetting

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        replaceFragment(LoginFormFragment())
        SavedSetting.loadLocale(this)
    }
    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            var transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.commit()
        }
    }
}