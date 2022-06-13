package com.example.mcommerce.ProductInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.model.Reviews
import com.example.mcommerce.R
import com.example.mcommerce.model.Image

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    var allComments: List<Reviews> = ArrayList<Reviews>()
    lateinit var context: Context
    fun setComment(allComments: List<Reviews>, context: Context) {
        this.allComments = allComments
        this.context = context
    }

    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtComment: TextView = itemView.findViewById(R.id.txtComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.review_layout, parent, false)
        return MyViewHolder(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPosition = allComments[position]
        holder.txtName.text=currentPosition.name
        holder.txtComment.text=currentPosition.comment

    }

    override fun getItemCount(): Int {
        return allComments.size
    }
}