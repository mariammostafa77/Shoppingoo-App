package com.example.mcommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PaymentActivity


class PayPalFragment : Fragment() {

    val clientKey = "ATWyXBtF8COKnCN1FG7AR_Sznijz2_WkTrhD7Cj2GzrwjVivPEacw2HE_AX_ndbR91_4dsEw0SEfrcuT"
    val PAYPAL_REQUEST_CODE = 123

    private val config = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientKey)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pay_pal, container, false)

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}
