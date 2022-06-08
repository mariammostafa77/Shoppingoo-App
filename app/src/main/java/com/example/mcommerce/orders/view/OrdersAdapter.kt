package com.example.mcommerce.orders.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.view.OnSubCategoryClickInterface

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>(){
    var allOrders:List<String> = ArrayList<String>()
    lateinit var context: Context

    fun setUpdatedData(allOrders:List<String>, context: Context){
        this.allOrders=allOrders
        this.context=context
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var tvOrderPrice: TextView =itemView.findViewById(R.id.tvOrderPrice)
        var tvOrderDate: TextView =itemView.findViewById(R.id.tvOrderDate)

        fun bind(data: String) {
            tvOrderPrice.text = allOrders[position]
            tvOrderDate.text = allOrders[position]

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return allOrders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allOrders.get(position).let { holder.bind(it) }
        Log.i("TAG","onBindViewHolder${allOrders.size}")

    }
}
