package com.example.mcommerce.categories.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.model.Product

class BrandProductsAdapter(var currencyConvertor: CurrencyConvertor) : RecyclerView.Adapter<BrandProductsAdapter.ViewHolder>(){
    var allBrands:List<Product> = ArrayList<Product>()
    //var variant:List<Variants> = ArrayList<Variants>()
    lateinit var context: Context
    lateinit var comminucator:Communicator

    fun setUpdatedData(allBrands:List<Product>,context: Context,comminucator:Communicator){
        this.allBrands=allBrands
        this.context=context
        this.comminucator=comminucator
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var productName:TextView=itemView.findViewById(R.id.productName)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        var catCardView:CardView=itemView.findViewById(R.id.cardViewCategoryItem)
        var tvProductPrice:TextView=itemView.findViewById(R.id.tvProductPrice)
        var favIconImage:ImageView=itemView.findViewById(R.id.favIconImage)

        fun bind(data: Product){
            productName.text=allBrands[position].title
            tvProductPrice.text=allBrands[position].variants[0].price + "EGP"
          //  tvProductPrice.text = currencyConvertor.onPriceConverter(position)
          //  Glide.with(context).load(allBrands[position].image.src).into(productImage)
            catCardView.setOnClickListener {
                comminucator.passProductData(allBrands[position])
            }
            favIconImage.setOnClickListener {
                currencyConvertor.addToFav(allBrands[position],favIconImage,position)
            }

            currencyConvertor.addFavImg(favIconImage,allBrands[position].variants[0].id)

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
