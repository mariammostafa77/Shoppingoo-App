package com.example.mcommerce.ProductInfo.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity
import com.example.mcommerce.HomeActivity.Companion.myFavFlag


import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.favourite.view.FavouriteFragment
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadCurrency
import com.example.mcommerce.model.Image
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.view.MysearchFragment
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModelFactory


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
    lateinit var addToFavImg:ImageView
    lateinit var backImg:ImageView
    lateinit var sizeSpinner:Spinner
    lateinit var colorSpinner:Spinner
    lateinit var btnAddToCard:Button
    var allFavProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    var allProducts:List<Product> = ArrayList<Product>()


    var count:Int=0
    var totalRate=0
    var price : Double = 0.0
    var currency : String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
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
        btnAddToCard=view.findViewById(R.id.btnAddToCard)
        addToFavImg=view.findViewById(R.id.addToFavImg)

        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
        val customerEmail = sharedPreferences.getString("email","").toString()

        val noteStatus = "fav"

        currency = loadCurrency(requireContext())
        //start
        Log.i("pro",output.id.toString())
        specificProductsFactory = ProductInfoViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        specificProductsViewModel = ViewModelProvider(this, specificProductsFactory).get(ProductInfoViewModel::class.java)

        specificProductsViewModel.getSpecificProducts(output.id.toString())
        specificProductsViewModel.onlineSpecificProducts.observe(viewLifecycleOwner) { product ->
            allProducts=listOf(product)


            Log.i("pro", "from fragment" + product.toString())
            specificProductsViewModel.onlineSpecificProducts.value?.let {
                specificProductsViewModel.getFavProducts()
                specificProductsViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
                    allFavProducts.addAll(favProducts)
                    for (i in 0..favProducts.size-1) {
                        if (favProducts.get(i).note == noteStatus && favProducts.get(i).email == customerEmail) {
                            if (product.variants[0].id == favProducts[i].line_items!![0].variant_id) {
                                addToFavImg.setImageResource(R.drawable.ic_favorite)

                            }
                        }
                    }

                }

                productInfoAdapter.setProductImages(product.images, requireContext())
                productName.text = product.title
                productDesc.text = product.body_html

                if (currency == getResources().getString(R.string.egp)) {
                    productPrice.text = "${product.variants[0].price} ${currency}"
                } else if (currency == getResources().getString(R.string.usd_t)) {
                    price = (product.variants[0].price.toDouble()) * 0.053
                    productPrice.text = "${price} ${currency}"
                } else if (currency == getResources().getString(R.string.eur_t_u20ac)) {
                    price = (product.variants[0].price.toDouble()) * 0.050
                    productPrice.text = "${price} ${currency}"
                } else {
                    productPrice.text =
                        "${product.variants[0].price} ${getResources().getString(R.string.egp)}"
                }

                for (i in 0..product.variants.size - 1) {
                    totalRate += product.variants[i].inventory_quantity
                }
                Log.i("totalRate", totalRate.toString())
                totalRate = totalRate / (product.variants.size)
                ratBar.rating = totalRate.toFloat() / 4

            }
            //spinner of color and size
            var sizeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item,
                product.options[0].values)
            var colorAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item,
                product.options[1].values)
            sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sizeSpinner.adapter = sizeAdapter
            colorSpinner.adapter = colorAdapter

            btnAddToCard.setOnClickListener {
                var variantId: Long = 0
                var order = DraftOrderX()
                order.note = "card"
                order.email = customerEmail
                for (i in 0..product.variants.size - 1) {
                    if (product.variants[i].option1 == sizeSpinner.getSelectedItem().toString() &&
                        product.variants[i].option2 == colorSpinner.getSelectedItem().toString()
                    ) {
                        variantId = product.variants[i].id
                        Log.i("Index", "index: " + variantId.toString())
                        // order.line_items!![0].variant_id = variantId
                        var lineItem = LineItem()
                        lineItem.quantity = Integer.parseInt(productCount.text.toString())
                        lineItem.variant_id = variantId
                        order.line_items = listOf(lineItem)
                        break
                    }
                }
                // order.line_items!![0].variant_id = 40335555395723
                var productImage = NoteAttribute()
                productImage.name = "image"
                productImage.value = product.images[0].src
                order.note_attributes = listOf(productImage)

                var draftOrder = DraftOrder(order)
                specificProductsViewModel.getCardOrder(draftOrder)
                specificProductsViewModel.onlineCardOrder.observe(viewLifecycleOwner) { cardOrder ->
                    if (cardOrder.isSuccessful) {
                        Toast.makeText(requireContext(),
                            "add to card successfull: " + cardOrder.code().toString(),
                            Toast.LENGTH_LONG).show()
                    } else {

                        Toast.makeText(requireContext(),
                            "add to card failed: " + cardOrder.code().toString(),
                            Toast.LENGTH_LONG).show()

                    }
                }


            }
            addToFavImg.setOnClickListener {

                specificProductsViewModel.getFavProducts()
                specificProductsViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
                    for (i in 0..favProducts.size-1) {
                        if (favProducts.get(i).note == noteStatus && favProducts.get(i).email == customerEmail) {
                            if (product.variants[0].id == favProducts[i].line_items!![0].variant_id){
                                if (favProducts[i].status == "open") {
                                    addToFavImg.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                    specificProductsViewModel.deleteFavProduct(favProducts.get(i).id.toString())
                                    specificProductsViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
                                        if (response.isSuccessful) {

                                            Toast.makeText(requireContext(),
                                                "Deleted Success!!!: " + response.code().toString(),
                                                Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(requireContext(),
                                                "Deleted failed: " + response.code().toString(),
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    addToFavImg.setImageResource(R.drawable.ic_favorite)
                                    var variantId: Long = 0
                                    var order = DraftOrderX()
                                    order.note = "fav"
                                    order.email = customerEmail
                                    variantId = product.variants[0].id
                                    Log.i("Index", "index: " + variantId.toString())
                                    // order.line_items!![0].variant_id = variantId
                                    var lineItem = LineItem()
                                    lineItem.quantity = Integer.parseInt(productCount.text.toString())
                                    lineItem.variant_id = variantId
                                    order.line_items = listOf(lineItem)

                                    // order.line_items!![0].variant_id = 40335555395723
                                    var productImage = NoteAttribute()
                                    productImage.name = "image"
                                    productImage.value = product.images[0].src
                                    order.note_attributes = listOf(productImage)

                                    var draftOrder = DraftOrder(order)
                                    specificProductsViewModel.getCardOrder(draftOrder)
                                    specificProductsViewModel.onlineCardOrder.observe(viewLifecycleOwner) { cardOrder ->
                                        if (cardOrder.isSuccessful) {
                                            Toast.makeText(requireContext(),
                                                "add to card successfull: " + cardOrder.code()
                                                    .toString(),
                                                Toast.LENGTH_LONG).show()

                                        } else {

                                            Toast.makeText(requireContext(),
                                                "add to card failed: " + cardOrder.code().toString(),
                                                Toast.LENGTH_LONG).show()

                                        }
                                    }
                                }
                            }
                        }
                    }

                }





            }

            //end
            productCount.text = "1"
            btnIncrease.setOnClickListener {
                //Toast.makeText(requireContext(),"test",Toast.LENGTH_LONG).show()
                count++
                productCount.text = count.toString()
            }
            btnDecrease.setOnClickListener {
                if (count > 0) {
                    count--
                } else {
                    count = 0
                }
                productCount.text = count.toString()

            }
            backImg.setOnClickListener {
                if (mySearchFlag == 1) {
                    val manager = activity!!.supportFragmentManager
                    val trans = manager.beginTransaction()
                    trans.replace(R.id.frameLayout, MysearchFragment()).commit()
                } else {
                    val manager = activity!!.supportFragmentManager
                    val trans = manager.beginTransaction()
                    trans.replace(R.id.frameLayout, CategoryFragment()).commit()
                }
            }
        }





        return view
    }


}