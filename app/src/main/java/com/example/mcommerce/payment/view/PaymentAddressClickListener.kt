package com.example.mcommerce.payment.view

import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.OrderPrices

interface PaymentAddressClickListener {

    fun goFromAddressToPayment(selectedAddress: Addresse, lineItems: ArrayList<LineItem>, orderPrices: ArrayList<OrderPrices>)

}