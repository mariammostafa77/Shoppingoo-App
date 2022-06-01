package com.example.mcommerce.ProductInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.R
import com.example.mcommerce.model.Image
import com.example.mcommerce.search.view.SearchAdapter

class ProductInfoAdapter :RecyclerView.Adapter<ProductInfoAdapter.MyViewHolder>() {

    var allProductImages:List<Image> = ArrayList<Image>()
    lateinit var context:Context
    fun setProductImages(allProductImages:List<Image>,context:Context){
        this.allProductImages=allProductImages
        this.context=context
        notifyDataSetChanged()
    }
    class MyViewHolder(private val itemView: View):RecyclerView.ViewHolder(itemView) {
        val productImg:ImageView=itemView.findViewById(R.id.adsImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item= LayoutInflater.from(parent.context).inflate(R.layout.ads_layout,parent,false)
        return MyViewHolder(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPosition=allProductImages[position]
        Glide.with(context).load(currentPosition.src).into(holder.productImg)

    }

    override fun getItemCount(): Int {
      return allProductImages.size
    }
}