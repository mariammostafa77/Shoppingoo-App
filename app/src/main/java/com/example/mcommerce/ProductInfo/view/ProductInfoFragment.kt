package com.example.mcommerce.ProductInfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.model.Image
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.view.MysearchFragment


class ProductInfoFragment : Fragment() {
    lateinit var output:Product
    lateinit var specificProduct: Product
    lateinit var productInfoRecyclerview: RecyclerView
    lateinit var productInfoAdapter: ProductInfoAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var specificProductsFactory: ProductInfoViewModelFactory
    lateinit var specificProductsViewModel: ProductInfoViewModel
    lateinit var productImgArrayList:ArrayList<Image>
    lateinit var productName:TextView
    lateinit var productPrice:TextView
    lateinit var productDesc:TextView
    lateinit var btnIncrease:Button
    lateinit var btnDecrease:Button
    lateinit var productCount:TextView
    lateinit var ratBar:RatingBar
    lateinit var backImg:ImageView
    lateinit var sizeSpinner:Spinner
    lateinit var colorSpinner:Spinner
    var count:Int=0
    var totalRate=0
    var price:String="EGP"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_product_info, container, false)
        output= arguments?.getSerializable("productInfo") as Product
        productImgArrayList=ArrayList<Image>()
        productInfoRecyclerview=view.findViewById(R.id.detailsRecyclerView)
        linearLayoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        productInfoRecyclerview.setLayoutManager(linearLayoutManager)
        productInfoAdapter= ProductInfoAdapter()
        productInfoRecyclerview.setAdapter(productInfoAdapter)
        productName=view.findViewById(R.id.txtProductName)
        btnIncrease=view.findViewById(R.id.btnAdd)
        btnDecrease=view.findViewById(R.id.btnMins)
        productCount=view.findViewById(R.id.txtQuantity)
        productPrice=view.findViewById(R.id.txtPrice)
        productDesc=view.findViewById(R.id.txtProductDetails)
        ratBar=view.findViewById(R.id.productRatingBar)
        backImg=view.findViewById(R.id.backImg)
        sizeSpinner=view.findViewById(R.id.sizeSpinner)
        colorSpinner=view.findViewById(R.id.colorSpiner)
        //start
        Log.i("pro",output.id.toString())
        specificProductsFactory = ProductInfoViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        specificProductsViewModel = ViewModelProvider(this, specificProductsFactory).get(ProductInfoViewModel::class.java)

        specificProductsViewModel.getSpecificProducts(output.id.toString())
        specificProductsViewModel.onlineSpecificProducts.observe(viewLifecycleOwner) { product ->
            Log.i("pro","from fragment"+product.toString())
            specificProductsViewModel.onlineSpecificProducts.value?.let {

                productInfoAdapter.setProductImages(product.images,requireContext())
                productName.text=product.title
                productDesc.text=product.body_html

                productPrice.text=product.variants[0].price + "EGP"
                for (i in 0..product.variants.size-1) {

                    totalRate+=product.variants[i].inventory_quantity
                }

                Log.i("totalRate",totalRate.toString())
                totalRate=totalRate/(product.variants.size)
                ratBar.rating=totalRate.toFloat()/4

            }
            //spinner of color and size
            var sizeAdapter:ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,product.options[0].values)
            var colorAdapter:ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,product.options[1].values)
            sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sizeSpinner.adapter=sizeAdapter
            colorSpinner.adapter=colorAdapter



        }

        //end
        productCount.text=count.toString()
        btnIncrease.setOnClickListener{
            //Toast.makeText(requireContext(),"test",Toast.LENGTH_LONG).show()
            count++
            productCount.text=count.toString()
        }
        btnDecrease.setOnClickListener{
            if(count>0) {
                count--
            }
            else{
                count=0
            }
            productCount.text=count.toString()

        }
        backImg.setOnClickListener {
            if(mySearchFlag==1) {
                val manager = activity!!.supportFragmentManager
                val trans = manager.beginTransaction()
                trans.replace(R.id.frameLayout, MysearchFragment()).commit()
            }
            else {
                val manager = activity!!.supportFragmentManager
                val trans = manager.beginTransaction()
                trans.replace(R.id.frameLayout, CategoryFragment()).commit()
            }
        }








        return view
    }


}