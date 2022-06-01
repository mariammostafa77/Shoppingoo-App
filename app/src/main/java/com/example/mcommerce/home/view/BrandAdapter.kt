package com.example.mcommerce.home.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.home.model.SmartCollection
import com.example.mcommerce.home.viewModel.HomeViewModel
import com.example.mcommerce.model.Product
import kotlin.math.log

class BrandAdapter() : RecyclerView.Adapter<BrandAdapter.ViewHolder>(){
    var allBrands:List<SmartCollection> = ArrayList<SmartCollection>()
    lateinit var context: Context
    lateinit var communicator: Communicator

    fun setUpdatedData(allBrands:List<SmartCollection>, context: Context,communicator: Communicator){
        this.allBrands=allBrands
        this.context=context
        this.communicator=communicator
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val brandImg: ImageView = itemView.findViewById(R.id.brandImg)
        val brandCard:CardView = itemView.findViewById(R.id.brandCard)
        fun bind(data: SmartCollection){
            Log.i("TAG","from onBind ${allBrands[position]}")
            Glide.with(context).load(allBrands[position].image.src).into(brandImg)

            brandCard.setOnClickListener(View.OnClickListener {
                communicator.goToProductInfo(allBrands[position].id.toString())


            })
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
