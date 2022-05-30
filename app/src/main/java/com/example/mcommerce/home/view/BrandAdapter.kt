package com.example.mcommerce.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.R
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.model.Product
import kotlin.math.log

class BrandAdapter : RecyclerView.Adapter<BrandAdapter.ViewHolder>(){
    var allBrands:List<SmartCollection> = ArrayList<SmartCollection>()
    lateinit var context: Context

    fun setUpdatedData(allBrands:List<SmartCollection>, context: Context){
        this.allBrands=allBrands
        this.context=context
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val brandImg: ImageView = itemView.findViewById(R.id.brandImg)
        val tvBrandName: TextView = itemView.findViewById(R.id.tvBrandName)
        fun bind(data: SmartCollection){
            Log.i("TAG","from onBind ${allBrands[position]}")
            tvBrandName.text=allBrands[position].title
            Glide.with(context).load(allBrands[position].image.src).into(brandImg)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_layout,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return allBrands.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allBrands.get(position).let { holder.bind(it) }
    }
}
