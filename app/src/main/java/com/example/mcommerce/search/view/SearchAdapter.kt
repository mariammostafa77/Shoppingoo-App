package com.example.mcommerce.search.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator

import com.example.mcommerce.R
import com.example.mcommerce.home.view.HomeFragmentDirections

import com.example.mcommerce.model.Product

class SearchAdapter( var comminicator:Communicator,var allProducts: List<Product>,var context: Context):RecyclerView.Adapter<SearchAdapter.MyViewHolder>(){

   // var allProducts: List<Product> = ArrayList<Product>()
    //lateinit var context: Context
    //lateinit var comminicator:Communicator
    fun setProductData(allProducts: List<Product>, context: Context,communicator: Communicator) {
        this.allProducts = allProducts
        this.context = context
        this.comminicator=comminicator
        notifyDataSetChanged()
    }

    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImg: ImageView = itemView.findViewById(R.id.productImage)
        val productTitle: TextView = itemView.findViewById(R.id.productName)
        val cardItem: CardView = itemView.findViewById(R.id.cardViewCategoryItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item= LayoutInflater.from(parent.context).inflate(R.layout.category_cell,parent,false)
        return MyViewHolder(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPosition=allProducts[position]
        holder.productTitle.text=currentPosition.title
        Glide.with(context).load(currentPosition.image.src).into(holder.productImg)
        holder.cardItem.setOnClickListener{

           //var navController: NavController = Navigation.findNavController(it)
         //  var navDir: NavDirections = MysearchFragmentDirections.actionSearchFragmentToProductFragment()
         //   navController.navigate(navDir)


            comminicator.passProductData(currentPosition)

        }
    }

    override fun getItemCount(): Int {
        return  allProducts.size
    }



}