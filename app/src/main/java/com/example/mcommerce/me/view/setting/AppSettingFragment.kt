package com.example.mcommerce.me.view.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.mcommerce.R
import java.util.*


class AppSettingFragment : Fragment() {

    lateinit var setting_back_icon: ImageView
    lateinit var userAddressCard : CardView
    lateinit var languageCard : CardView

    lateinit var txtAppLanguage : TextView

    companion object{
        var isUserLogin = true
        var languageSelected: String = "English"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_setting, container, false)
        initComponent(view)
        loadLocale()
      //  txtAppLanguage.text = languageSelected
        userAddressCard.setOnClickListener {
            replaceFragment(UserAddressesFragment())
        }
        languageCard.setOnClickListener {
            showLanguagesList()
        }
        return view
    }

    private fun initComponent(view: View){
        setting_back_icon = view.findViewById(R.id.setting_back_icon)
        userAddressCard = view.findViewById(R.id.userAddressCard)
        languageCard = view.findViewById(R.id.languageCard)

    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

    @SuppressLint("ResourceType")
    private fun showLanguagesList(){
        val languagesList = arrayOf("English","Arabic")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setSingleChoiceItems(languagesList,-1){ dialog, which ->
            when (which){
                0 -> {
                    setLocale("en")
                    replaceFragment(AppSettingFragment())
                  // languageSelected = getResources().getStringArray(R.string.english).toString()
                }
                1 -> {
                    setLocale("ar")
                    replaceFragment(AppSettingFragment())
                 //   languageSelected = "العربية"
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocale(lang: String) {
        var locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireContext().resources.updateConfiguration(config,requireContext().resources.displayMetrics)
        val editor = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
        editor.putString("language", lang)
        editor.apply()
    }
    private  fun loadLocale() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languages: String? = sharedPreferences.getString("language", "")
        setLocale(languages!!)
    }


}