package com.example.mcommerce.categories.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.HomeActivity.Companion.mySearchFlag
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.categories.viewModel.CategoriesViewFactory
import com.example.mcommerce.categories.viewModel.CategoriesViewModel
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.NoteAttribute
import com.example.mcommerce.me.viewmodel.CustomerViewModel
import com.example.mcommerce.me.viewmodel.CustomerViewModelFactory
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Product
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.search.viewModel.SearchViewModel
import com.example.mcommerce.search.viewModel.SearchViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.FieldPosition

class CategoryFragment : Fragment() ,OnSubCategoryClickInterface, CurrencyConvertor {
    private lateinit var brandProductsAdapter: BrandProductsAdapter
    private lateinit var subCategoriesAdapter: SubCategoriesAdapter
    private lateinit var categoriesProductFactory: CategoriesViewFactory
    private lateinit var categoriesProductViewModel: CategoriesViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoriesTabLayout: TabLayout
    private lateinit var searchIcon:ImageView
    private lateinit var filterImg:ImageView
    private lateinit var communicator:Communicator
    private lateinit var categoryBarTitle:TextView
    private lateinit var dialog : BottomSheetDialog
    lateinit var searchFactor: SearchViewModelFactory
    lateinit var searchViewModel: SearchViewModel
    private var id:String=""
    private var brandName:String=""
    private var subCategorySelected:String=""
    var allVariantsID:ArrayList<Long> = ArrayList<Long>()
    var allFavProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()

    lateinit var customerViewModel: CustomerViewModel
    lateinit var customerViewModelFactory: CustomerViewModelFactory

     var userId = ""
    var toCurrency = ""
    var convertorResult: Double = 0.0
    var allProducts: ArrayList<Product> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_category, container, false)
        initComponents(view)
        searchFactor = SearchViewModelFactory(
            Repository.getInstance(AppClient.getInstance(), requireContext())
        )
        searchViewModel = ViewModelProvider(this, searchFactor).get(SearchViewModel::class.java)
        subCategoriesAdapter= SubCategoriesAdapter()
        brandProductsAdapter= BrandProductsAdapter(this)
        categoryRecyclerView.adapter = brandProductsAdapter
        communicator = activity as Communicator
        customerViewModelFactory = CustomerViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        customerViewModel = ViewModelProvider(this, customerViewModelFactory).get(CustomerViewModel::class.java)
        val sharedPreferences = requireContext().getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")

        //  userId = sharedPreferences.getString("cusomerID","").toString()

        id=""
       //checkArgs()
        categoriesProductFactory = CategoriesViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        categoriesProductViewModel = ViewModelProvider(this, categoriesProductFactory)[CategoriesViewModel::class.java]
        categoryBarTitle.text=brandName
        filterImg.setOnClickListener(View.OnClickListener {
            val view = layoutInflater.inflate(R.layout.custom_bottom_sheet, null)
            val subTypeRecycle:RecyclerView=view.findViewById(R.id.subTypeRecycle)
            subTypeRecycle.adapter = subCategoriesAdapter
            dialog.setContentView(view)
            dialog.show()
        })
        categoriesProductViewModel.getAllProducts(brandName,"","")
        categoriesProductViewModel.onlineProductsTypes.observe(viewLifecycleOwner) {
            getProductTypes(it)
        }
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        categoriesProductViewModel.onlinesubcategoriesProduct.observe(viewLifecycleOwner) {products ->
            allProducts.addAll(products)
            brandProductsAdapter.setUpdatedData(products,requireContext(),communicator)
        }
        categoriesProductViewModel.allOnlineProducts.observe(viewLifecycleOwner) {
            getProductTypes(it)
        }
        categoriesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               onTabSelectedListener(tab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        searchIcon.setOnClickListener {
            mySearchFlag=2
            //communicator.goToSearchWithID(id)
            communicator.goToSearchWithAllData(id,brandName,subCategorySelected)

        }
        searchViewModel.getFavProducts()
        allFavProducts.clear()
        searchViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
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

    private fun checkArgs() {
        Log.i("TAG","Brand name $brandName")
        if(arguments != null){
            brandName=arguments?.getString("brandTitle").toString()
        }else{
            brandName=""
        }
    }

    override fun onStart() {
        super.onStart()
        checkArgs()
        id=""
        subCategorySelected=""
        //categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
    }



    override fun onSubCategoryClick(type:String) {
        subCategorySelected=type
        categoriesProductViewModel.getCategories(brandName,subCategorySelected,id)
        Toast.makeText(requireContext(),subCategorySelected,Toast.LENGTH_LONG).show()
        dialog.dismiss()
    }
    override fun onPriceConverter(position: Int) : String{
        /*
           toCurrency = SavedSetting.loadCurrency(context!!)
            if(toCurrency.isNullOrEmpty()){
                toCurrency = "EGP"
            }
            customerViewModel.getAmountAfterConversion(toCurrency)
            customerViewModel.onlineCurrencyChanged.observe(viewLifecycleOwner) { result ->
                convertorResult = result.result
            }
       // Log.i("Testttttttttt", (allProducts.get(position).variants.get(0).price.toDouble() * convertorResult).toString() +" " +toCurrency)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        val result = (allProducts.get(position).variants.get(0).price.toDouble() * convertorResult)
        val roundoff = df.format(result)
        return "${roundoff}  ${toCurrency}"
*/
        return ""
    }

    override fun addToFav(product: Product, img: ImageView, myIndex: Int) {
       /* Toast.makeText(requireContext(),"Fav clicked",Toast.LENGTH_LONG).show()
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")

        if(allProducts[myIndex].variants[0].id in allVariantsID){
            Log.i("exits","already exists")

            img.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            searchViewModel.deleteSelectedProduct(allFavProducts.get(myIndex).id.toString())
            searchViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
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
            img.setImageResource(R.drawable.ic_favorite)
            var variantId: Long = 0
            var order = DraftOrderX()
            order.note = "fav"
            order.email = email

            variantId = allProducts[myIndex].variants[0].id
            Log.i("Index", "index: " + variantId.toString())
            // order.line_items!![0].variant_id = variantId
            var lineItem = LineItem()
            lineItem.quantity = 1
            lineItem.variant_id = variantId
            order.line_items = listOf(lineItem)


            // order.line_items!![0].variant_id = 40335555395723
            var productImage = NoteAttribute()
            productImage.name = "image"
            productImage.value = allProducts[myIndex].images[0].src
            order.note_attributes = listOf(productImage)

            var draftOrder = DraftOrder(order)
            searchViewModel.getCardOrder(draftOrder)
            searchViewModel.onlineCardOrder.observe(viewLifecycleOwner) { cardOrder ->
                if (cardOrder.isSuccessful) {
                    Toast.makeText(requireContext(),
                        "add to card successfull: " + cardOrder.code().toString(),
                        Toast.LENGTH_LONG).show()
                } else {

                    Toast.makeText(requireContext(),
                        "add to card failed: " + cardOrder.code().toString()+"//"+cardOrder.body(),
                        Toast.LENGTH_LONG).show()

                }
            }

        }*/



    }

    override fun addFavImg(img: ImageView, id: Long) {
        /*val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")
        searchViewModel.getFavProducts()
        searchViewModel.onlineFavProduct.observe(viewLifecycleOwner) { favProducts ->
            for (i in 0..favProducts.size-1) {
                if (favProducts.get(i).note == "fav" && favProducts.get(i).email == email) {
                    if (id == favProducts[i].line_items!![0].variant_id) {
                        img.setImageResource(R.drawable.ic_favorite)

                    }

                }
            }



        }*/
    }

    private fun initComponents(view:View){
        categoriesTabLayout = view.findViewById(R.id.categoryTabBar)
        categoryRecyclerView=view.findViewById(R.id.categoryRecyclerView)
        searchIcon=view.findViewById(R.id.search_icon)
        categoryBarTitle=view.findViewById(R.id.categoryBarTitle)
        filterImg=view.findViewById(R.id.filterImg)
        dialog = BottomSheetDialog(requireContext())

    }
    private fun getProductTypes(allProductsTypes:List<Product>){
        val typesSet= mutableSetOf<String>()
        if(allProductsTypes.isNotEmpty()){
            for(element in allProductsTypes){
                typesSet.add(element.product_type)
            }
        }
        subCategoriesAdapter.setUpdatedData(typesSet.toList(),requireContext(),this)
    }
    private fun onTabSelectedListener(tab: TabLayout.Tab){
        when (tab.position) {
            0 -> {
                id=""
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            1 -> {
                id="273053712523"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            2 -> {
                id="273053679755"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            3 -> {
                id="273053745291"
                Log.i("TAG","")
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            4 -> {
                id="273053778059"
                categoriesProductViewModel.getCategories(brandName,"",id)
                getSubTypes()
                true
            }
            else -> false

        }
    }
    private fun getSubTypes() {
        if (id.isNotEmpty()) {
            categoriesProductViewModel.getProductsType(id)
        }
        else{
            categoriesProductViewModel.getAllProducts(brandName,"","")
        }
    }
}