package com.example.mcommerce.ProductInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mcommerce.R
import com.example.mcommerce.home.view.AdsAdapter
import com.example.mcommerce.model.Image

class ProductViewPagerAdapter :
    RecyclerView.Adapter<ProductViewPagerAdapter.ImageViewHolder>() {
    var allProductImages:List<Image> = ArrayList<Image>()
    lateinit var context:Context
    fun setProductImages(allProductImages:List<Image>,context:Context){
        this.allProductImages=allProductImages
        this.context=context
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImg: ImageView = itemView.findViewById(R.id.adsImg);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val item= LayoutInflater.from(parent.context).inflate(R.layout.ads_layout,parent,false)
        return ImageViewHolder(item)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentPosition=allProductImages[position]
        Glide.with(context).load(currentPosition.src).into(holder.productImg)
    }

    override fun getItemCount(): Int {
        return allProductImages.size
    }

    private val runnable = Runnable {
//        imageList.addAll(imageList)
//        notifyDataSetChanged()
    }
}