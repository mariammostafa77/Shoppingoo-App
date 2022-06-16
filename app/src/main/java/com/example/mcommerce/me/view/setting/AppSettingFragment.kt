package com.example.mcommerce.me.view.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.AuthActivity
import com.example.mcommerce.R
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadCurrency
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadLocale
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.setCurrency
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.setCurrencyResult
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.setLocale
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_view.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class AppSettingFragment : Fragment() {

    lateinit var setting_back_icon: ImageView
    lateinit var userAddressCard: CardView
    lateinit var languageCard: CardView
    lateinit var currencyCard: CardView
    lateinit var contactUsCard: CardView
    lateinit var shareAppCard: CardView

    lateinit var txtSelectedLanguage: TextView
    lateinit var txtLastAddress: TextView
    lateinit var txtSignOutText: TextView
    lateinit var txtCurrency : TextView
    lateinit var currencySpinner: Spinner

    lateinit var customerViewModel: CustomerViewModel
    lateinit var customerViewModelFactory: CustomerViewModelFactory
    val spinnerArray: ArrayList<String> = ArrayList()

    var convertorResult : Double = 1.0

    companion object {
        var languageSelected: String = ""
        var currencySelected: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_setting, container, false)
        initComponent(view)
        loadLocale(requireContext())
        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)

        customerViewModel.getAllCurrencies()
        customerViewModel.onlineCurrencies.observe(viewLifecycleOwner) { currencies ->
            for (i in 0..currencies.size - 1) {
                spinnerArray.add(currencies.get(i).currency)
            }
        }
        currencySelected = loadCurrency(context!!)
        txtCurrency.text = currencySelected
        txtSelectedLanguage.text = languageSelected
        spinnerArray.add("EGP")
        val currencyAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = currencyAdapter

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currencySelected = parent?.getItemAtPosition(position).toString()
                setCurrency(currencySelected, context!!)
                customerViewModel.getAmountAfterConversion(parent?.getItemAtPosition(position).toString())
                customerViewModel.onlineCurrencyChanged.observe(viewLifecycleOwner) { result ->
                    convertorResult = result.result
                    setCurrencyResult(convertorResult.toString(),requireContext())
                }
            }
        }
        currencySelected = loadCurrency(requireContext())

        userAddressCard.setOnClickListener {
            replaceFragment(UserAddressesFragment())
        }
        languageCard.setOnClickListener {
            showLanguagesList()
        }
        contactUsCard.setOnClickListener {
            showContactUsDialog()
        }
        shareAppCard.setOnClickListener {
            shareOurApp()
        }
        txtSignOutText.setOnClickListener {
            val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
            val editor = requireContext().getSharedPreferences("userAuth", Context.MODE_PRIVATE).edit()
            val isLogin = sharedPreferences.getBoolean("isLogin", true)
            if (isLogin == true) {
                editor.remove("email")
                editor.remove("password")
                editor.remove("fname")
                editor.remove("lname")
                editor.remove("phone")
                editor.remove("cusomerID")
                editor.putBoolean("isLogin", false)
                editor.commit()
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val currencyAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.dropdwon_currency_item, spinnerArray)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.setAdapter(currencyAdapter)
    }

    private fun initComponent(view: View) {
        setting_back_icon = view.findViewById(R.id.setting_back_icon)
        userAddressCard = view.findViewById(R.id.userAddressCard)
        languageCard = view.findViewById(R.id.languageCard)
        currencyCard = view.findViewById(R.id.currencyCard)
        contactUsCard = view.findViewById(R.id.contactUsCard)
        shareAppCard = view.findViewById(R.id.shareAppCard)

        txtSelectedLanguage = view.findViewById(R.id.txtSelectedLanguage)
       // txtLastAddress = view.findViewById(R.id.txtLastAddress)
        txtSignOutText = view.findViewById(R.id.txtSignOutText)
        txtCurrency = view.findViewById(R.id.txtCurrency)
        currencySpinner = view.findViewById(R.id.currencySpinner)
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @SuppressLint("ResourceType")
    private fun showLanguagesList() {
        val languagesList = arrayOf("English", "Arabic")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setSingleChoiceItems(languagesList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    setLocale("en", requireContext())
                    replaceFragment(AppSettingFragment())
                    languageSelected = getResources().getString(R.string.english)
                }
                1 -> {
                    setLocale("ar", requireContext())
                    replaceFragment(AppSettingFragment())
                    languageSelected = getResources().getString(R.string.arabic)
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun showContactUsDialog() {
        val view = View.inflate(requireContext(), R.layout.dialog_view, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        view.btn_confirm.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "asmaayousef786@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "mcommerce_contact_us")
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