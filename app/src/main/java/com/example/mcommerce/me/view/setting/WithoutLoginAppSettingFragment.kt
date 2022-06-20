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
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.R
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import kotlinx.android.synthetic.main.dialog_view.view.*

class WithoutLoginAppSettingFragment : Fragment() {

    lateinit var setting_back_icon_without_login: ImageView
    lateinit var txtSelectedLanguageWithoutLogin: TextView
    lateinit var languageCardWithoutLogin: CardView
    lateinit var contactUsCardWithoutLogin: CardView
    lateinit var shareAppCardWithoutLogin: CardView

    companion object {
        var languageSelected: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_without_login_app_setting, container, false)

        initComponent(view)
        SavedSetting.loadLocale(requireContext())

        txtSelectedLanguageWithoutLogin.text = languageSelected

        languageCardWithoutLogin.setOnClickListener {
            showLanguagesList()
        }
        contactUsCardWithoutLogin.setOnClickListener {
            showContactUsDialog()
        }
        shareAppCardWithoutLogin.setOnClickListener {
            shareOurApp()
        }

        return view
    }

    private fun initComponent(view: View) {
        setting_back_icon_without_login = view.findViewById(R.id.setting_back_icon_without_login)
        languageCardWithoutLogin = view.findViewById(R.id.languageCardWithoutLogin)
        contactUsCardWithoutLogin = view.findViewById(R.id.contactUsCardWithoutLogin)
        shareAppCardWithoutLogin = view.findViewById(R.id.shareAppCardWithoutLogin)
        txtSelectedLanguageWithoutLogin = view.findViewById(R.id.txtSelectedLanguageWithoutLogin)

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

    @SuppressLint("ResourceType")
    private fun showLanguagesList() {
        val languagesList = arrayOf("English", "Arabic")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setSingleChoiceItems(languagesList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    SavedSetting.setLocale("en", requireContext())
                    replaceFragment(WithoutLoginAppSettingFragment())
                    WithLoginAppSettingFragment.languageSelected = getResources().getString(R.string.english)
                }
                1 -> {
                    SavedSetting.setLocale("ar", requireContext())
                    replaceFragment(WithoutLoginAppSettingFragment())
                    WithLoginAppSettingFragment.languageSelected = getResources().getString(R.string.arabic)
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}