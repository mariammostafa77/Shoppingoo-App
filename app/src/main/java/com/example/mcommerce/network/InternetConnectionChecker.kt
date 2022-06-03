package com.example.mcommerce.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class InternetConnectionChecker(var context: Context) {

    fun isConnected(): Boolean{
        var connected = false
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected.
            also { connected = it }) {
            Log.i("con", "connected : $connected")
            return connected
        }
        return connected
    }

}