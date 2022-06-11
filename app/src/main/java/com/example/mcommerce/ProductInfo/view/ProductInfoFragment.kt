package com.example.mcommerce.ProductInfo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.myDetailsFlag
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.categories.view.CategoryFragment
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting.Companion.loadCurrency
import com.example.mcommerce.model.Image
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.view.MysearchFragment
import java.math.RoundingMode
import java.text.DecimalFormat


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
    var allVariantsID:ArrayList<Long> = ArrayList<Long>()
    var allProducts:List<Product> = ArrayList<Product>()
    var isExists=false
    var myIndex:Long=0

    var count:Int=0
    var totalRate=0
    var price : Double = 0.0
    var currency : String = ""
    var productIndex:Int=0
    var toCurrency  = ""
    var convertorResult : Double = 1.0
    var productID:Long=0

    lateinit var customerViewModel: CustomerViewModel
    lateinit var customerViewModelFactory: CustomerViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        var view =inflater.inflate(R.layout.fragment_product_info, container, false)
        Log.i("test","test")

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
        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)

        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
        val customerEmail = sharedPreferences.getString("email","").toString()
        val userId = sharedPreferences.getString("cusomerID","").toString()
        currency = loadCurrency(requireContext())
        val noteStatus = "fav"
        if(myDetailsFlag==1){
           productID= arguments?.getLong("productID",0)!!
        }
        else {
            output= arguments?.getSerializable("productInfo") as Product
            productID = output.id
        }

        //start

        specificProductsFactory = ProductInfoViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        specificProductsViewModel = ViewModelProvider(this, specificProductsFactory).get(ProductInfoViewModel::class.java)

        specificProductsViewModel.getSpecificProducts(productID.toString())
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
           /*
            toCurrency = loadCurrency(context!!)
            if(toCurrency.isNullOrEmpty()){
                toCurrency = "EGP"
            }
            customerViewModel.getAmountAfterConversion(toCurrency)
            customerViewModel.onlineCurrencyChanged.observe(viewLifecycleOwner) { result ->
                convertorResult = result.result
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.UP
                val result = product.variants[0].price.toDouble() * convertorResult
                val roundoff = df.format(result)
                productPrice.text = "${roundoff}  ${toCurrency}"
            }
                */

                productPrice.text = product.variants[0].price
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


        }
        btnAddToCard.setOnClickListener {
            var variantId: Long = 0
            var order = DraftOrderX()
            order.note = "card"
            order.email = customerEmail
            for (i in 0..allProducts[0].variants.size - 1) {
                if (allProducts[0].variants[i].option1 == sizeSpinner.getSelectedItem().toString() &&
                    allProducts[0].variants[i].option2 == colorSpinner.getSelectedItem().toString()
                ) {
                    variantId = allProducts[0].variants[i].id
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
            productImage.value = allProducts[0].images[0].src
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

        specificProductsViewModel.getFavProducts()
        allFavProducts.clear()
        specificProductsViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
            allFavProducts.clear()
            allVariantsID.clear()
            for (i in 0..favProducts.size - 1) {
                if (favProducts.get(i).note == noteStatus && favProducts.get(i).email == customerEmail) {
                    allFavProducts.add(favProducts.get(i))
                    allVariantsID.add(favProducts.get(i).line_items!![0].variant_id!!)
                }
            }

        }
        addToFavImg.setOnClickListener {

            if(allProducts[0].variants[0].id in allVariantsID){
                Log.i("exits","already exists")
                for(i in 0..allFavProducts.size-1) {
                    if(allFavProducts[i].line_items!![0].variant_id==allProducts[0].variants[0].id){
                        productIndex=i
                        break
                    }
                }
                addToFavImg.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                specificProductsViewModel.deleteFavProduct(allFavProducts.get(productIndex).id.toString())
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

            }
            else{
                Log.i("exits","not exists")
                addToFavImg.setImageResource(R.drawable.ic_favorite)
                var variantId: Long = 0
                var order = DraftOrderX()
                order.note = "fav"
                order.email = customerEmail
                variantId = allProducts[0].variants[0].id
                Log.i("Index", "index: " + variantId.toString())
                // order.line_items!![0].variant_id = variantId
                var lineItem = LineItem()
                lineItem.quantity = Integer.parseInt(productCount.text.toString())
                lineItem.variant_id = variantId
                order.line_items = listOf(lineItem)

                // order.line_items!![0].variant_id = 40335555395723
                var productImage = NoteAttribute()
                productImage.name = "image"
                productImage.value = allProducts[0].images[0].src
                order.note_attributes = listOf(productImage)

                var draftOrder = DraftOrder(order)
                specificProductsViewModel.getCardOrder(draftOrder)
                specificProductsViewModel.onlineCardOrder.observe(viewLifecycleOwner) { fav ->
                    if (fav.isSuccessful) {
                        Toast.makeText(requireContext(),"add to card successfull: " + fav.code().toString(),
                            Toast.LENGTH_LONG).show()

                    } else {

                        Toast.makeText(requireContext(),
                            "add to card failed: " + fav.code().toString(),
                            Toast.LENGTH_LONG).show()

                    }
                }
            }
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

        return view
    }

}