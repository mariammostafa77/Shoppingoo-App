package com.example.mcommerce.brandProducts.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.R
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.model.Product

class BrandProductsAdapter : RecyclerView.Adapter<BrandProductsAdapter.ViewHolder>(){
    var allBrands:List<Product> = ArrayList<Product>()
    lateinit var context: Context

    fun setUpdatedData(allBrands:List<Product>, context: Context){
        this.allBrands=allBrands
        this.context=context
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var productName:TextView=itemView.findViewById(R.id.productName)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        fun bind(data: Product){
            Log.i("TAG","from onBind ${allBrands[position]}")
            productName.text=allBrands[position].title
            Glide.with(context).load(allBrands[position].image.src).into(productImage)

        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_cell,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return allBrands.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allBrands.get(position).let { holder.bind(it) }
    }
}
