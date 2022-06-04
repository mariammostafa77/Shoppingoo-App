package com.example.mcommerce.me.view.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat
import com.example.mcommerce.R
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadCurrency
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.setCurrency
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.setLocale
import kotlinx.android.synthetic.main.dialog_view.view.*



class AppSettingFragment : Fragment() {

    lateinit var setting_back_icon: ImageView
    lateinit var userAddressCard : CardView
    lateinit var languageCard : CardView
    lateinit var currencyCard : CardView
    lateinit var contactUsCard : CardView
    lateinit var shareAppCard : CardView

    lateinit var txtSelectedLanguage : TextView
    lateinit var txtSelectedCurrency: TextView
    lateinit var txtLastAddress: TextView

    companion object{
        var isUserLogin = true
        var languageSelected: String = ""
        var currencySelected : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_setting, container, false)
        initComponent(view)
        SavedSetting.loadLocale(requireContext())
        currencySelected = loadCurrency(requireContext())
        if (currencySelected.isNullOrEmpty() || languageSelected.isNullOrEmpty()){
            currencySelected = getResources().getString(R.string.egp)
            languageSelected = resources.getString(R.string.system_default)
        }
        txtSelectedLanguage.text = languageSelected
        txtSelectedCurrency.text = currencySelected
        userAddressCard.setOnClickListener {
            replaceFragment(UserAddressesFragment())
        }
        languageCard.setOnClickListener {
            showLanguagesList()
        }
        currencyCard.setOnClickListener {
            showCurrencyList()
        }
        contactUsCard.setOnClickListener {
            showContactUsDialog()
        }
        shareAppCard.setOnClickListener {
            shareOurApp()
        }
        return view
    }

    private fun initComponent(view: View){
        setting_back_icon = view.findViewById(R.id.setting_back_icon)
        userAddressCard = view.findViewById(R.id.userAddressCard)
        languageCard = view.findViewById(R.id.languageCard)
        currencyCard = view.findViewById(R.id.currencyCard)
        contactUsCard = view.findViewById(R.id.contactUsCard)
        shareAppCard = view.findViewById(R.id.shareAppCard)

        txtSelectedLanguage = view.findViewById(R.id.txtSelectedLanguage)
        txtSelectedCurrency = view.findViewById(R.id.txtSelectedCurrency)
        txtLastAddress = view.findViewById(R.id.txtLastAddress)

    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @SuppressLint("ResourceType")
    private fun showLanguagesList(){
        val languagesList = arrayOf("English","Arabic")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setSingleChoiceItems(languagesList,-1){ dialog, which ->
            when (which){
                0 -> {
                    setLocale("en",requireContext())
                    replaceFragment(AppSettingFragment())
                   languageSelected = getResources().getString(R.string.english)
                }
                1 -> {
                    setLocale("ar",requireContext())
                    replaceFragment(AppSettingFragment())
                    languageSelected = getResources().getString(R.string.arabic)
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun showCurrencyList(){
        val languagesList = getResources().getStringArray(R.array.currency_options)
        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setSingleChoiceItems(languagesList,-1){ dialog, which ->
            when (which) {
                0 ->{
                    currencySelected  = getResources().getString(R.string.egp)
                    setCurrency(currencySelected,requireContext())
                    replaceFragment(AppSettingFragment())
                }
                1 ->{
                    currencySelected = getResources().getString(R.string.usd_t)
                    setCurrency(currencySelected,requireContext())
                    replaceFragment(AppSettingFragment())
                }
                2 ->{
                    currencySelected = getResources().getString(R.string.eur_t_u20ac)
                    setCurrency(currencySelected,requireContext())
                    replaceFragment(AppSettingFragment())
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun showContactUsDialog(){
        val view = View.inflate(requireContext(), R.layout.dialog_view, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        view.btn_confirm.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "asmaayousef786@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT,"mcommerce_contact_us")
            startActivity(intent)

        }
        view.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun shareOurApp() {
        val txtShare = "M_Commerce App"
        val shareLink = "http://play.google.com/store/apps/details?id=com.example.mcommerce"
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, "${txtShare} \n${shareLink}")
        startActivity(share)
    }


}