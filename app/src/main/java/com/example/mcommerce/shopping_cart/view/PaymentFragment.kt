package com.example.mcommerce.shopping_cart.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.mcommerce.orders.model.ShippingAddress
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.home.viewModel.HomeViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.getUserName
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.model.OrderResponse
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModelFactory
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadCurrency
import com.google.android.libraries.places.internal.it
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import java.util.*


class PaymentFragment : Fragment() {

    lateinit var paymentTitleTxt: TextView
    lateinit var txtSubTotalText: TextView
    lateinit var txtFeesText: TextView
    lateinit var txtTotalText: TextView
    lateinit var etCouponsField: EditText
    lateinit var btnApplyDiscount: Button
    lateinit var btnPlaceOrder: Button
    lateinit var radioCash: RadioButton
    lateinit var radioVisa: RadioButton
    lateinit var txtDiscountCount: TextView

    lateinit var couponsFactory: HomeViewModelFactory
    lateinit var couponsViewModel: HomeViewModel

    lateinit var shoppingCartViewModelFactory: ShoppingCartViewModelFactory
    lateinit var shoppingCartViewModel: ShoppingCartViewModel

    lateinit var communicator: Communicator
    lateinit var selectedAddress: Addresse

    var lineItems: ArrayList<LineItem> = ArrayList()
    var orderPrices: ArrayList<OrderPrices> = ArrayList()

    var paymentMethod: String = "Cash"
    var subTotal: Double = 0.0
    var total: Double = 0.0
    var fees: Double = 0.0
    var discount: Double = 0.0
    var userEmail: String = ""
    var discountCode: String = ""
    var totoalAmount: String = ""
    var subTotoalAmount: String = ""
    var taxAmount: String = ""
    //// Stripe
    val SECRET_KEY =
        "sk_test_51LAg1sALiJRoQbXz8YOr3C8y0Na1dCdhJHjTTXZyFmo5tfS2MJAHkU6z5a5gNMXsXPglc9aI4nYaJvX2awqPi9sD00692V2P2U"
    val PUBLISH_KEY =
        "pk_test_51LAg1sALiJRoQbXzMKxTAusXlWsckrb7W169998HBwF4FPbB0is5cwr25k6JFh85dp9OOBOigmQcrz5A4dYHXbiH00Z126GnBL"
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerId: String
    lateinit var ephericalKey: String
    lateinit var clientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        initComponent(view)
        communicator = activity as Communicator

        PaymentConfiguration.init(requireContext(), PUBLISH_KEY)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        couponsFactory =
            HomeViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        couponsViewModel = ViewModelProvider(this, couponsFactory).get(HomeViewModel::class.java)

        shoppingCartViewModelFactory = ShoppingCartViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()
            )
        )
        shoppingCartViewModel = ViewModelProvider(
            this,
            shoppingCartViewModelFactory
        ).get(ShoppingCartViewModel::class.java)

        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        userEmail = sharedPreferences.getString("email", "").toString()

        if (arguments != null) {
            selectedAddress = arguments?.getSerializable("selectedAddress") as Addresse
            lineItems = arguments?.getSerializable("lineItems") as ArrayList<LineItem>
            orderPrices = arguments?.getSerializable("orderPrice") as ArrayList<OrderPrices>
        }

        calculateOrderPrice()
        ///Asign Variable
        paymentTitleTxt.append(" ${getUserName(requireContext())}")

        subTotoalAmount = SavedSetting.getPrice(subTotal.toString(), requireContext())
        totoalAmount = SavedSetting.getPrice(total.toString(), requireContext())
        taxAmount = SavedSetting.getPrice(fees.toString(), requireContext())

        txtSubTotalText.text = subTotoalAmount
        txtTotalText.text = totoalAmount
        txtFeesText.text = taxAmount

        btnApplyDiscount.setOnClickListener {
            applyDiscount(it)
        }
        radioCash.setOnClickListener {
            paymentMethod = "Cash"
        }
        radioVisa.setOnClickListener {
            paymentMethod = "Visa"
            val request: StringRequest =
                object : StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                    Response.Listener { response ->
                        try {
                            val jsonObject = JSONObject(response)
                            customerId = jsonObject.getString("id")
                            // Toast.makeText(requireContext(), "Customer Id: " + customerId, Toast.LENGTH_SHORT).show()
                            getEphericalKey(customerId)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener {
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val header: HashMap<String, String> = HashMap<String, String>()
                        header.put("Authorization", "Bearer $SECRET_KEY")
                        return header
                    }
                }
            val requestQueue = Volley.newRequestQueue(requireContext())
            requestQueue.add(request)
        }

        btnPlaceOrder.setOnClickListener {
            paymentFlow()
        }
        return view
    }

    private fun initComponent(view: View) {
        paymentTitleTxt = view.findViewById(R.id.paymentTitleTxt)
        txtSubTotalText = view.findViewById(R.id.txtSubTotalText)
        txtFeesText = view.findViewById(R.id.txtFeesText)
        txtTotalText = view.findViewById(R.id.txtTotalText)
        etCouponsField = view.findViewById(R.id.etCouponsField)
        btnApplyDiscount = view.findViewById(R.id.btnApplyDiscount)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)
        radioCash = view.findViewById(R.id.radioCash)
        radioVisa = view.findViewById(R.id.radioVisa)
        txtDiscountCount = view.findViewById(R.id.txtDiscountCount)
    }

    private fun calculateOrderPrice() {
        for (i in 0..orderPrices.size - 1) {
            subTotal += orderPrices.get(i).subTotal
            total += orderPrices.get(i).total
            fees += orderPrices.get(i).tax
        }
    }

    private fun applyDiscount(view: View) {
        var discountCode = etCouponsField.text.toString()
        couponsViewModel.onlineDiscountCodes.observe(viewLifecycleOwner) { coupons ->
            if (coupons != null) {
                for (i in 0..coupons.size - 1) {
                    if (coupons[i].code.equals(discountCode)) {
                        btnApplyDiscount.setText("Verified!")
                        etCouponsField.setEnabled(false)
                        discountCode = etCouponsField.text.toString()
                        txtDiscountCount.text = (total * 0.1).toString()
                        total = total.toDouble() - txtDiscountCount.text.toString().toDouble()
                        txtTotalText.text =
                            SavedSetting.getPrice(total.toString(), requireContext())
                        break
                    } else {
                        val snake = Snackbar.make(view, "Invalid Coupons.", Snackbar.LENGTH_LONG)
                        snake.show()

                    }
                }
            }
        }
    }

    companion object {
        val clientKey =
            "ATWyXBtF8COKnCN1FG7AR_Sznijz2_WkTrhD7Cj2GzrwjVivPEacw2HE_AX_ndbR91_4dsEw0SEfrcuT"

        // val PAYPAL_REQUEST_CODE = 123
        val PAYPAL_REQUEST_CODE = 7171
        private val config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clientKey)
    }

    /// Stripe Methods
    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            val order = Order()
            order.email = userEmail
            val shippingAddress = ShippingAddress(
                address1 = selectedAddress.address1.toString(), address2 = selectedAddress.address2.toString(),
                city = selectedAddress.city.toString(), country = selectedAddress.country.toString(),
                name = getUserName(requireContext()), phone = selectedAddress.phone.toString(),
                zip = selectedAddress.zip.toString(),
                company = "", country_code = selectedAddress.country_code.toString())
            order.shipping_address = shippingAddress
            //  order.discount_codes = listOf(discountCode)
            order.processing_method = paymentMethod
            order.line_items = lineItems as List<com.example.mcommerce.orders.model.LineItem>
            val orderResponse = OrderResponse(order)
            shoppingCartViewModel.postNewOrder(orderResponse)
            shoppingCartViewModel.onlineNewOrder.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Order Added Successfully. ", Toast.LENGTH_LONG
                    ).show()
                    communicator.goToOrderSummary(response.body()?.order!!, totoalAmount, subTotoalAmount, taxAmount)
                    //response.body()?.order?.let { it1 -> communicator.goToOrderSummary(it1,fees) }
                } else {
                    Toast.makeText(requireContext(), "Order Not Placed.", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getEphericalKey(id: String) {
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        ephericalKey = jsonObject.getString("id")
                        getClientSecret(customerId, ephericalKey)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header.put("Authorization", "Bearer $SECRET_KEY")
                    header.put("Stripe-Version", "2020-08-27")
                    return header
                }

                override fun getParams(): Map<String, String> {
                    val param: HashMap<String, String> = HashMap<String, String>()
                    param.put("customer", customerId)
                    return param
                }
            }

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }

    private fun getClientSecret(customerId: String, ephericalKey: String) {
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        clientSecret = jsonObject.getString("client_secret")
                        Toast.makeText(
                            requireContext(),
                            "Client Secret: " + clientSecret,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    //
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: HashMap<String, String> = HashMap<String, String>()
                    header.put("Authorization", "Bearer $SECRET_KEY")
                    return header
                }

                override fun getParams(): Map<String, String> {
                    val param: HashMap<String, String> = HashMap<String, String>()
                    param.put("customer", customerId)
                    val str = total.toString()
                    val delim = "."
                    val list = str.split(delim)
                    param.put("amount", "${list.get(0)}${list.get(1)}0")
                    param.put("currency", loadCurrency(requireContext()))
                    param.put("automatic_payment_methods[enabled]", "true")
                    return param
                }
            }

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }

    private fun paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
            clientSecret, PaymentSheet.Configuration(
                "ITI",
                PaymentSheet.CustomerConfiguration(customerId, ephericalKey)
            )
        )
    }


}