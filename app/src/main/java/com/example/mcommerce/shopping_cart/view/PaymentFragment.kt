package com.example.mcommerce.shopping_cart.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.OrderPrices
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.home.viewModel.HomeViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.getUserName
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

class PaymentFragment : Fragment() {

    lateinit var paymentTitleTxt: TextView
    lateinit var txtSubTotalText: TextView
    lateinit var txtFeesText: TextView
    lateinit var txtTotalText: TextView
    lateinit var etCouponsField : EditText
    lateinit var btnApplyDiscount :Button
    lateinit var btnPlaceOrder : Button
    lateinit var radioPaypal : RadioButton
    lateinit var radioCash : RadioButton

    lateinit var couponsFactory: HomeViewModelFactory
    lateinit var couponsViewModel: HomeViewModel
    lateinit var communicator: Communicator
    lateinit var selectedAddress: Addresse

   // var amount = ""
    var lineItems : ArrayList<LineItem> = ArrayList()
    var orderPrices : ArrayList<OrderPrices> = ArrayList()

    var paymentMethod: String = "Cash"
    var subTotal : Double = 0.0
    var total : Double = 0.0
    var fees : Double = 0.0
    var discount: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        initComponent(view)
        communicator = activity as Communicator

        couponsFactory = HomeViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        couponsViewModel = ViewModelProvider(this, couponsFactory).get(HomeViewModel::class.java)

        if(arguments != null){
            selectedAddress = arguments?.getSerializable("selectedAddress") as Addresse
            lineItems = arguments?.getSerializable("lineItems") as ArrayList<LineItem>
            orderPrices = arguments?.getSerializable("orderPrice") as ArrayList<OrderPrices>
            Log.i("payment","payment From Fragment: ${selectedAddress.city}")
        }
        /////
        calculateOrderPrice()
        ///Asign Variable
        paymentTitleTxt.append(" ${getUserName(requireContext())}")
        txtSubTotalText.text = subTotal.toString()
        txtTotalText.text = total.toString()
        txtFeesText.text = fees.toString()

        btnApplyDiscount.setOnClickListener {
            applyDiscount()
        }
        radioPaypal.setOnClickListener {
            paymentMethod = "Paypal"
            getPayment()
        }
        radioCash.setOnClickListener {
            paymentMethod = "Cash"
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm: PaymentConfirmation =
                    data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)!!
                if (confirm != null) {
                    try {
                        val paymentDetails: String = confirm.toJSONObject().toString(4)
                        val payObj: JSONObject = JSONObject(paymentDetails)
                        val payID: String = payObj.getJSONObject("response").getString("id")
                        val state: String = payObj.getJSONObject("response").getString("state")
                        Toast.makeText(requireContext(), "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e)
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        }
    }

    private fun initComponent(view: View){
        paymentTitleTxt = view.findViewById(R.id.paymentTitleTxt)
        txtSubTotalText = view.findViewById(R.id.txtSubTotalText)
        txtFeesText = view.findViewById(R.id.txtFeesText)
        txtTotalText = view.findViewById(R.id.txtTotalText)
        etCouponsField = view.findViewById(R.id.etCouponsField)
        btnApplyDiscount = view.findViewById(R.id.btnApplyDiscount)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)
        radioPaypal = view.findViewById(R.id.radioPaypal)
        radioCash = view.findViewById(R.id.radioCash)
    }

    private fun calculateOrderPrice(){
        for (i in 0..orderPrices.size-1){
            subTotal += orderPrices.get(i).subTotal
            total += orderPrices.get(i).total
            fees += orderPrices.get(i).tax
        }
    }

    private fun applyDiscount(){
        val discountCode = etCouponsField.text.toString()
        couponsViewModel.onlineDiscountCodes.observe(viewLifecycleOwner) { coupons ->
            if (coupons != null){
                for(i in 0..coupons.size-1){
                    if(coupons[i].code.equals(discountCode)){
                        btnApplyDiscount.setText("Verified!")
                        // etCouponsField.isInEditMode = false
                        etCouponsField.setEnabled(false)
                        break
                    }
                    else{
                        Toast.makeText(requireContext() ,"Invalid Coupons.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getPayment() {
        val amount: String = "50.00"
        val payment = PayPalPayment(BigDecimal(amount), "USD", "Course Fees",
            PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(requireContext(), PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    companion object{
        val clientKey = "ATWyXBtF8COKnCN1FG7AR_Sznijz2_WkTrhD7Cj2GzrwjVivPEacw2HE_AX_ndbR91_4dsEw0SEfrcuT"
        val PAYPAL_REQUEST_CODE = 123
        private val config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientKey)
    }

}