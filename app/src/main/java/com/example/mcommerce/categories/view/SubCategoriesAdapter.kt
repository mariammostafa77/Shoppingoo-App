package com.example.mcommerce.categories.view

import android.content.Context
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.model.Product


class SubCategoriesAdapter : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>(){
    var allTypes:List<String> = ArrayList<String>()
    lateinit var context: Context
    lateinit var onClickAction: OnSubCategoryClickInterface

    fun setUpdatedData(allTypes:List<String>,context: Context,onClickAction: OnSubCategoryClickInterface){
        this.allTypes=allTypes
        this.onClickAction=onClickAction
        this.context=context
        notifyDataSetChanged()
       // Log.i("TAG","adapter ${allTypes[0]}")
    }
    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        var tvType: TextView =itemView.findViewById(R.id.tvType)
        var subCategoryConstraintLayout : ConstraintLayout=itemView.findViewById(R.id.subCategoryConstraintLayout)

        fun bind(data: String) {
            tvType.text = allTypes[position]
            Log.i("TAG","adapter on bind ${allTypes[position]}")
            tvType.setOnClickListener(View.OnClickListener {
                onClickAction.onSubCategoryClick(allTypes[position])
                //subCategoryConstraintLayout.background= ContextCompat.getDrawable(context, R.color.orange)

            })
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_item,parent,false);
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return allTypes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allTypes.get(position).let { holder.bind(it) }
        Log.i("TAG","onBindViewHolder${allTypes.size}")

    }
}
