package com.example.mcommerce.categories.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.R
import com.example.mcommerce.home.model.SmartCollection

class BrandsAdapter : RecyclerView.Adapter<BrandsAdapter.ViewHolder>(){
    var brandNames:List<SmartCollection> = ArrayList<SmartCollection>()
    lateinit var context: Context
    lateinit var onClickAction: OnBrandNameClickListener

    fun setUpdatedData(allTypes:List<SmartCollection>, context: Context, onClickAction: OnBrandNameClickListener){
        this.brandNames=allTypes
        this.onClickAction=onClickAction
        this.context=context
        notifyDataSetChanged()
        Log.i("TAGGGGGG",allTypes.toString())
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var tvBrandName: TextView =itemView.findViewById(R.id.tvBrandName)

        fun bind(data: SmartCollection) {
            if(brandNames[position].title?.isEmpty() == true){
                tvBrandName.text = "All Brands"
            }else{
                tvBrandName.text = brandNames[position].title
            }
            tvBrandName.setOnClickListener(View.OnClickListener {
                notifyDataSetChanged()
                onClickAction.onBrandNameClick(brandNames[position].title!!)
            })
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_name_item,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return brandNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        brandNames.get(position).let { holder.bind(it) }

    }
}
