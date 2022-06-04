package com.example.mcommerce.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.model.DiscountCode

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
        holder.couponsLayout.setOnClickListener {
            setClipboard(context,holder.discountCode.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return codes.size
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
        val couponsLayout: ConstraintLayout = itemView.findViewById(R.id.couponsLayout)
        val discountCode: TextView = itemView.findViewById(R.id.txtDiscountCode)
    }

    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = text
            Toast.makeText(context,"Text Copied!", Toast.LENGTH_SHORT).show()
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Text Copied!", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context,"Text Copied!", Toast.LENGTH_SHORT).show()
        }
    }


}