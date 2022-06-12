package com.example.mcommerce.me.view.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.auth.model.Addresse
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.OrderPrices

class CustomerAddressAdapter(var communicator: Communicator, var lineItems: ArrayList<LineItem>, var orderPrices: ArrayList<OrderPrices>)
    : RecyclerView.Adapter<CustomerAddressAdapter.ViewHolder>(){
    var customerAddresses :List<Addresse> = ArrayList<Addresse>()
    lateinit var context: Context

    fun setCustomerAddressesData(context: Context, _customerAddresses: List<Addresse>){
        this.context= context
        customerAddresses = _customerAddresses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_item,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.countryText.append("  ${customerAddresses[position].country}")
        holder.cityText.append("  ${customerAddresses[position].city}")
        holder.userAddressLine1.append("  ${customerAddresses[position].address1}")
        holder.phoneText.append("  ${customerAddresses[position].phone}")
        holder.addressCardView.setOnClickListener {
            //////// move to checkout with Address Data
            val selectedAddresses: Addresse = customerAddresses[position]
            communicator.goToPaymentFromAddress(selectedAddresses,lineItems,orderPrices)
        }
    }
    override fun getItemCount(): Int {
        return customerAddresses.size
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
       val countryText: TextView = itemView.findViewById(R.id.userCountryText)
        val cityText: TextView = itemView.findViewById(R.id.userCityText)
        val userAddressLine1: TextView = itemView.findViewById(R.id.userAddressLine1)
        val phoneText: TextView = itemView.findViewById(R.id.userPhoneText)
        val addressCardView: CardView = itemView.findViewById(R.id.addressCardView)
    }

}