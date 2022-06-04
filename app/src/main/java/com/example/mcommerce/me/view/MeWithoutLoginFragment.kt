package com.example.mcommerce.me.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.mcommerce.R
import com.example.mcommerce.me.view.setting.AppSettingFragment

class MeWithoutLoginFragment : Fragment() {

    lateinit var withoutLoginSettingICon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_me_without_login, container, false)
        withoutLoginSettingICon = view.findViewById(R.id.withoutLoginSettingICon)
        withoutLoginSettingICon.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, AppSettingFragment())
            transaction.addToBackStack(null);
            transaction.commit()
        }
        return view
    }

}