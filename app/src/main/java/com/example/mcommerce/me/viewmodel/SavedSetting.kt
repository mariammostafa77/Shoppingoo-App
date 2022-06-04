package com.example.mcommerce.me.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.mcommerce.R
import com.example.mcommerce.me.view.setting.AppSettingFragment.Companion.languageSelected
import java.util.*

class SavedSetting {

    companion object{
         fun setLocale(lang: String, context: Context) {
            var locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
             context.resources.updateConfiguration(config,context.resources.displayMetrics)
            val editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
            editor.putString("language", lang)
            editor.apply()
        }
          fun loadLocale(context: Context) {
              val sharedPreferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
              val language: String? = sharedPreferences.getString("language", "")
              languageSelected = language.toString()
              setLocale(language!!,context)
        }

        fun setCurrency(selectedCurrency: String, context: Context) {
            val editor = context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                editor.putString("currency", selectedCurrency)
                editor.apply()
        }
        fun loadCurrency(context: Context) : String{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val currency: String? = sharedPreferences.getString("currency","")
            setCurrency(currency!!,context)
            return currency
        }

    }

}