package com.example.mcommerce.favourite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrderX

class FavProductsAdapter(private val listener: FavouriteOnClickLisner,var comminicator: Communicator) : RecyclerView.Adapter<FavProductsAdapter.ViewHolder>(){
    var allFavProducts:List<DraftOrderX> = ArrayList<DraftOrderX>()
    lateinit var context: Context
    var arrayCount:Int=0


    fun setFavtProducts(context: Context, allFavProducts:List<DraftOrderX>, arrayCount:Int){
        this.context= context
        this.allFavProducts = allFavProducts
        this.arrayCount=arrayCount
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourite_item,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.favProductTitle.text = allFavProducts[position].line_items?.get(0)!!.title
        holder.favProductPrice.text=allFavProducts[position].line_items?.get(0)!!.price
        Glide.with(context).load(allFavProducts[position].note_attributes?.get(0)?.value).into(holder.favProductImg)


        holder.deleteFav.setOnClickListener {

            listener.onItemClickListener(allFavProducts[position])
        }

        holder.favCartItem.setOnClickListener {
            allFavProducts[position].line_items!![0].product_id?.let { it1 ->
                comminicator.goToProductDetails(it1)
            }
        }


    }

    override fun getItemCount(): Int {
        return arrayCount
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
        val favCartItem : CardView = itemView.findViewById(R.id.favCartItem)
        val favProductImg : ImageView = itemView.findViewById(R.id.favProductImg)
        val favProductTitle : TextView = itemView.findViewById(R.id.favProductTitle)
        val favProductPrice : TextView = itemView.findViewById(R.id.favProductPrice)
        val deleteFav : ImageView = itemView.findViewById(R.id.deleteFav)

    }
}