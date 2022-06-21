package com.example.mcommerce.search.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.AuthActivity
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.viewModel.SearchViewModel
import com.example.mcommerce.search.viewModel.SearchViewModelFactory
import kotlinx.android.synthetic.main.dialog_view.view.*
import java.util.*
import kotlin.collections.ArrayList


class MysearchFragment : Fragment(),FavClicked {
    lateinit var communicator: Communicator
    lateinit var productSearchRecyclerview:RecyclerView
    lateinit var productSearchAdapter: SearchAdapter
    lateinit var searchFactor:SearchViewModelFactory
    lateinit var searchViewModel:SearchViewModel
    lateinit var productInfoFactor: ProductInfoViewModelFactory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var noDataSearchImg:ImageView
    lateinit var noDataSearchtxt:TextView
    lateinit var categoriesProductFactory: CategoriesViewFactory
    lateinit var categoriesProductViewModel: CategoriesViewModel
//lateinit var linearLayoutManager:LinearLayoutManager
    lateinit var edtSearch:AutoCompleteTextView
    lateinit var btnSearch:Button
    lateinit var output:String
    var productID:String=""
    var brandName:String=""
    var subCatName:String=""
    var allFavProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    var allVariantsID:ArrayList<Long> = ArrayList<Long>()
    var allProducts:List<Product> = ArrayList<Product>()
    lateinit var email:String

    lateinit var allProductArrayList:ArrayList<Product>
    lateinit var filterProductArrayList:ArrayList<Product>
    var productsName:ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_mysearch, container, false)
        communicator = activity as Communicator
        noDataSearchImg=view.findViewById(R.id.noDataSearchImg)
        noDataSearchtxt=view.findViewById(R.id.txtNoSearchData)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
         email= sharedPreferences.getString("email","").toString()

        edtSearch=view.findViewById(R.id.edtSearch)
        btnSearch=view.findViewById(R.id.btnSearch)
        filterProductArrayList= ArrayList<Product>()

        productSearchRecyclerview=view.findViewById(R.id.searhProductRecyclerView)
        //linearLayoutManager= LinearLayoutManager(requireContext())
        //productSearchRecyclerview.setLayoutManager(linearLayoutManager)
        productInfoFactor = ProductInfoViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        productInfoViewModel = ViewModelProvider(this, productInfoFactor).get(ProductInfoViewModel::class.java)

        productSearchAdapter= SearchAdapter(communicator,filterProductArrayList,requireContext(),this)
        productSearchRecyclerview.setAdapter(productSearchAdapter)

            searchFactor = SearchViewModelFactory(
                Repository.getInstance(AppClient.getInstance(), requireContext())
            )
           allProductArrayList = ArrayList<Product>()
            searchViewModel = ViewModelProvider(this, searchFactor).get(SearchViewModel::class.java)
            searchViewModel.getAllProducts()
            if(mySearchFlag==1) {
                searchViewModel.onlineProducts.observe(viewLifecycleOwner) { product ->
                 allProducts=product
                    if (allProductArrayList.size == 0) {
                        allProductArrayList.addAll(product)

                    }
                    productsName.clear()
                    for(i in 0..product.size-1) {
                        productsName.add(product[i].title)
                    }
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,productsName)
                    edtSearch.threshold=1
                    edtSearch.setAdapter(adapter)
                    filterProductArrayList.clear()
                    edtSearch.setOnItemClickListener { adapterView, view, i, l ->
                        filterProductArrayList.clear()
                        Toast.makeText(requireContext(),"clicked"+ adapter.getItem(i).toString(),Toast.LENGTH_LONG).show()
                        var productName:String= adapter.getItem(i).toString()
                        if(productName.isNotEmpty()){

                            allProductArrayList.forEach{
                                if(it.title.contains(productName))
                                {
                                    filterProductArrayList.add(it)
                                }
                            }
                            if(filterProductArrayList.isEmpty()){
                                noDataSearchImg.visibility=View.VISIBLE
                                noDataSearchtxt.visibility=View.VISIBLE
                            }
                            else{
                                noDataSearchtxt.visibility=View.INVISIBLE
                                noDataSearchImg.visibility=View.INVISIBLE
                            }

                        }
                        else{
                            filterProductArrayList.clear()
                        }

                        productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)



                    }


                }

            }
        else if(mySearchFlag==2){

                output= arguments?.getString("catID").toString()
                productID= arguments?.getString("catID").toString()
                brandName= arguments?.getString("brandName").toString()
                subCatName= arguments?.getString("subCatName").toString()
                categoriesProductFactory = CategoriesViewFactory(
                    Repository.getInstance(
                        AppClient.getInstance(),
                        requireContext()))
                categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory).get(CategoriesViewModel::class.java)
                categoriesProductViewModel.getCategories(brandName,subCatName,productID)
                categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner)  {

                    Log.i("TAG","Count  ${it.size}")

                    allProductArrayList.addAll(it)
                    productsName.clear()
                    it.forEach {

                        productsName.add(it.title)
                    }
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,productsName)
                    edtSearch.threshold=1
                    edtSearch.setAdapter(adapter)
                    filterProductArrayList.clear()
                    edtSearch.setOnItemClickListener { adapterView, view, i, l ->
                        filterProductArrayList.clear()
                      //  Toast.makeText(requireContext(),"clicked"+ adapter.getItem(i).toString(),Toast.LENGTH_LONG).show()
                        var productName:String= adapter.getItem(i).toString()
                        if(productName.isNotEmpty()){

                            allProductArrayList.forEach{
                                if(it.title.contains(productName))
                                {
                                    filterProductArrayList.add(it)
                                }
                            }
                            if(filterProductArrayList.isEmpty()){
                                noDataSearchImg.visibility=View.VISIBLE
                                noDataSearchtxt.visibility=View.VISIBLE
                            }
                            else{
                                noDataSearchImg.visibility=View.INVISIBLE
                                noDataSearchtxt.visibility=View.INVISIBLE
                            }

                        }
                        else{
                            filterProductArrayList.clear()
                        }

                        productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)



                    }


                }



            }
        btnSearch.setOnClickListener{
            filterProductArrayList.clear()
            var productName:String=edtSearch.text.toString().toLowerCase(Locale.getDefault())


                if(productName.isNotEmpty()){

                    allProductArrayList.forEach{
                        if(it.title.toLowerCase(Locale.getDefault()).contains(productName))
                        {
                            filterProductArrayList.add(it)
                        }
                    }
                    if(filterProductArrayList.isEmpty()){
                        noDataSearchImg.visibility=View.VISIBLE
                        noDataSearchtxt.visibility=View.VISIBLE
                    }
                    else{
                        noDataSearchImg.visibility=View.INVISIBLE
                        noDataSearchtxt.visibility=View.INVISIBLE
                    }

                }
                else{
                    filterProductArrayList.clear()
                }



            productSearchAdapter.setProductData(filterProductArrayList,requireContext(),activity as Communicator)


        }


        productInfoViewModel.getFavProducts()
        allFavProducts.clear()
        productInfoViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
            allFavProducts.clear()
            allVariantsID.clear()
            for (i in 0..favProducts.size - 1) {
                if (favProducts.get(i).note == "fav" && favProducts.get(i).email == email) {
                    allFavProducts.add(favProducts.get(i))
                    allVariantsID.add(favProducts.get(i).line_items!![0].variant_id!!)
                }
            }

        }

        return view
    }

    override fun addToFav(product:Product,img: ImageView,myIndex:Int) {
        if(email!="") {
            Toast.makeText(requireContext(), "Fav clicked", Toast.LENGTH_LONG).show()
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
            val email: String? = sharedPreferences.getString("email", "")
            Log.i("filter", "size of filter product: " + filterProductArrayList.size)
            Log.i("filter", "size of fav product: " + allFavProducts.size)
            Log.i("filter", "size of variants product: " + allVariantsID.size)


            if (filterProductArrayList[myIndex].variants[0].id in allVariantsID) {
                Log.i("exits", "already exists")

                img.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                productInfoViewModel.deleteFavProduct(allFavProducts.get(myIndex).id.toString())
                productInfoViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
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
                Log.i("exits", "not exists")
                img.setImageResource(R.drawable.ic_favorite)
                var variantId: Long = 0
                var order = DraftOrderX()
                order.note = "fav"
                order.email = email

                variantId = filterProductArrayList[myIndex].variants[0].id
                Log.i("Index", "index: " + variantId.toString())
                // order.line_items!![0].variant_id = variantId
                var lineItem = LineItem()
                lineItem.quantity = 1
                lineItem.variant_id = variantId
                order.line_items = listOf(lineItem)


                // order.line_items!![0].variant_id = 40335555395723
                var productImage = NoteAttribute()
                productImage.name = "image"
                productImage.value = filterProductArrayList[myIndex].images[0].src
                order.note_attributes = listOf(productImage)

                var draftOrder = DraftOrder(order)
                productInfoViewModel.getCardOrder(draftOrder)
                productInfoViewModel.onlineCardOrder.observe(viewLifecycleOwner) { cardOrder ->
                    if (cardOrder.isSuccessful) {
                        Toast.makeText(requireContext(),
                            "add to card successfull: " + cardOrder.code().toString(),
                            Toast.LENGTH_LONG).show()
                    } else {

                        Toast.makeText(requireContext(),
                            "add to card failed: " + cardOrder.code()
                                .toString() + "//" + cardOrder.body(),
                            Toast.LENGTH_LONG).show()

                    }
                }

            }


        }
        else{
            showLoginDialog("You can not add to Favourite without Login",requireContext())
        }


    }

    override fun addFavImg(img: ImageView,id: Long) {

            val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
            val email: String? = sharedPreferences.getString("email","")
            if(id in allVariantsID){
                img.setImageResource(R.drawable.ic_favorite)
            }
    }
    fun showLoginDialog(dialogInfo: String,context:Context) {
        val view = View.inflate(context, R.layout.dialog_view, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        view.dialoImg.setImageResource(R.drawable.personlogin)
        view.btn_confirm.text = "LOGIN"
        view.btn_cancel.text = "CANCEL"
        view.txtDialogTitle.text = "Warning"
        view.txtDialogSubTitle.text = dialogInfo
        view.txtDialogSubTitle.setTypeface(view.txtDialogSubTitle.getTypeface(), Typeface.ITALIC);
        view.txtDialogInfo.text = ""
        view.btn_confirm.setOnClickListener {
            startActivity(Intent(context, AuthActivity::class.java))
        }
        view.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }



}