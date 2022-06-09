package com.example.mcommerce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("userAuth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000L)
            if(sharedPreferences.getBoolean("isLogin",false)== false) {
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }
        }


    }
}