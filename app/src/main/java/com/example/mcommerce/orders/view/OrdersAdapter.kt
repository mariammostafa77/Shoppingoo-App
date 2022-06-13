package com.example.mcommerce.orders.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.orders.model.Order

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>(){
    var allOrders:List<Order> = ArrayList<Order>()
    lateinit var context: Context
    lateinit var onItemClickListener: OnOrderClickListenerInterface
    var orderSize:Int=0

    fun setUpdatedData(allOrders:List<Order>, context: Context,onItemClickListener: OnOrderClickListenerInterface,orderSize:Int){
        this.allOrders=allOrders
        this.context=context
        this.onItemClickListener=onItemClickListener
        this.orderSize=orderSize
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var tvOrderPrice: TextView =itemView.findViewById(R.id.tvOrderPrice)
        var tvOrderDate: TextView =itemView.findViewById(R.id.tvOrderDate)
        var orderCard:CardView=itemView.findViewById(R.id.orderCard)
        fun bind(data: Order) {
            tvOrderPrice.text = allOrders[position].total_price
            tvOrderDate.text = allOrders[position].created_at
            orderCard.setOnClickListener(View.OnClickListener {
                onItemClickListener.onOrderClickListener(allOrders[position])
            })

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return orderSize
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allOrders.get(position).let { holder.bind(it) }

    }
}
