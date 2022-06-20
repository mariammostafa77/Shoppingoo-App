package com.example.mcommerce.orderDetails.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.orders.model.LineItem

class OrderItemsAdapter : RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>(){
    var productInOrder:List<LineItem> = ArrayList<LineItem>()
    lateinit var context: Context
    lateinit var comminucator: Communicator

    fun setUpdatedData(productInOrder: List<LineItem>, context: Context, communicator: Communicator){
        this.productInOrder=productInOrder
        this.comminucator=communicator
        this.context=context
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var tvItemTitle: TextView =itemView.findViewById(R.id.tvItemTitle)
        var tvItemPrice : TextView =itemView.findViewById(R.id.tvItemPrice)
        var tvQuantity:TextView=itemView.findViewById(R.id.tvQuantity)

        fun bind(data: LineItem) {
            val amount = SavedSetting.getPrice(productInOrder[position].price.toString(), context)
            tvItemPrice.text = amount
            tvItemTitle.text = productInOrder[position].title
            tvQuantity.text=productInOrder[position].quantity.toString()
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_in_order,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return productInOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        productInOrder.get(position).let { holder.bind(it) }
        Log.i("TAG","onBindViewHolder${productInOrder.size}")

    }
}
