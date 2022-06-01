package com.example.mcommerce.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.DiscountCode
import com.example.mcommerce.model.DiscountCodesModel


class DiscountCodeAdapter : RecyclerView.Adapter<DiscountCodeAdapter.ViewHolder>(){
    var codes :List<DiscountCode> = ArrayList<DiscountCode>()
    lateinit var context: Context

    fun setCouponsData(context: Context, _codes: List<DiscountCode>){
        this.context= context
        codes = _codes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.discount_codes_layout,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.discountCode.text = codes[position].code
    }

    override fun getItemCount(): Int {
        return codes.size
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
      //  val discountImage: ImageView = itemView.findViewById(R.id.discountCodeImg)
        val discountCode: TextView = itemView.findViewById(R.id.txtDiscountCode)
    }


}