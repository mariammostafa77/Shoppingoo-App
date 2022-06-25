package com.example.mcommerce.me.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModel
import com.example.mcommerce.ProductInfo.viewModel.ProductInfoViewModelFactory
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.favourite.view.FavProductsAdapter
import com.example.mcommerce.favourite.view.FavouriteFragment
import com.example.mcommerce.favourite.view.FavouriteOnClickLisner
import com.example.mcommerce.me.view.setting.WithLoginAppSettingFragment
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.network.CheckInternetConnectionFirstTime
import com.example.mcommerce.network.InternetConnectionChecker
import com.example.mcommerce.orders.model.Order
import com.example.mcommerce.orders.view.OnOrderClickListenerInterface
import com.example.mcommerce.orders.view.OrdersAdapter
import com.example.mcommerce.orders.view.OrdersFragment
import com.example.mcommerce.orders.viewModel.OrdersViewFactory
import com.example.mcommerce.orders.viewModel.OrdersViewModel
import com.example.mcommerce.shopping_cart.view.ShoppingCartFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.remove_layout.view.*

class MeWithLogin : Fragment(), FavouriteOnClickLisner, OnOrderClickListenerInterface {

    lateinit var settingICon: ImageView
    lateinit var shoppingCartIcon : ImageView
    lateinit var txtWelcomeUser : TextView
    lateinit var txtNoFav:TextView
    lateinit var txtMoreFav:TextView
    lateinit var tvNoOrder:TextView
    lateinit var tvMoreOrders : TextView
    lateinit var ordersrecycler:RecyclerView
    lateinit var favRecyclerView: RecyclerView
    lateinit var favAdapter: FavProductsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var favViewModelFactory : ProductInfoViewModelFactory
    lateinit var favViewModel: ProductInfoViewModel
    lateinit var communicator: Communicator
    lateinit var meLoginNoInternetView:ConstraintLayout
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var ordersViewFactory: OrdersViewFactory
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var internetConnectionChecker: InternetConnectionChecker
    var favProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    var userName : String = ""
    private var userId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_me_with_login, container, false)
        initComponent(view)
        txtNoFav=view.findViewById(R.id.txtNoFav)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val fname: String? = sharedPreferences.getString("fname","")
        val lname: String? = sharedPreferences.getString("lname","")
        userId = sharedPreferences.getString("cusomerID","").toString()
        tvNoOrder.visibility=View.INVISIBLE
        //order array
        ordersAdapter=OrdersAdapter()
        ordersrecycler.adapter=ordersAdapter
        ordersViewFactory = OrdersViewFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()))
        ordersViewModel = ViewModelProvider(this, ordersViewFactory)[OrdersViewModel::class.java]
        favViewModelFactory = ProductInfoViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        favViewModel = ViewModelProvider(this, favViewModelFactory).get(ProductInfoViewModel::class.java)


        if(CheckInternetConnectionFirstTime.checkForInternet(requireContext())){
            if(userId.isNotEmpty()){
                ordersViewModel.getAllOrders(userId)
            }
            favViewModel.getFavProducts()
            meLoginNoInternetView.visibility=View.INVISIBLE
        }else{
            meLoginNoInternetView.visibility=View.VISIBLE
        }
        internetConnectionChecker = InternetConnectionChecker(requireContext())
        internetConnectionChecker.observe(this,{ isConnected ->
            if (isConnected){
                if(userId.isNotEmpty()){
                    ordersViewModel.getAllOrders(userId)
                }
                favViewModel.getFavProducts()
                meLoginNoInternetView.visibility=View.INVISIBLE
            }else{
                var snake = Snackbar.make(view, "Ops! You Lost internet connection!!!", Snackbar.LENGTH_LONG)
                snake.show()
            }
        })



        ordersViewModel.allOnlineOrders.observe(viewLifecycleOwner) {
            if(it.size >=2){
                ordersAdapter.setUpdatedData(it,requireContext(),this,2)
                tvNoOrder.visibility=View.INVISIBLE
            }
            else{
                ordersAdapter.setUpdatedData(it,requireContext(),this,it.size)
               if(it.isEmpty()){
                   tvNoOrder.visibility=View.VISIBLE
                   tvMoreOrders.visibility=View.INVISIBLE
               }else{
                   tvNoOrder.visibility=View.INVISIBLE
                   tvMoreOrders.visibility=View.VISIBLE
               }
            }

        }
        //favourite arrayList
        communicator = activity as Communicator
        favRecyclerView = view.findViewById(R.id.wishListRecyclerview)
        favAdapter= FavProductsAdapter(this,communicator)
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        favRecyclerView.setLayoutManager(linearLayoutManager)
        favRecyclerView.setAdapter(favAdapter)


        val email: String? = sharedPreferences.getString("email","")
        val note = "fav"
        favViewModel.onlineFavProduct.observe(viewLifecycleOwner) { allFavProducts ->
            favProducts.clear()
            for (i in 0..allFavProducts.size-1){
                if(allFavProducts.get(i).note == note && allFavProducts.get(i).email == email){
                    favProducts.add(allFavProducts.get(i))
                }

            }
            if(!favProducts.isEmpty() ){
                txtMoreFav.visibility=View.VISIBLE
                txtNoFav.visibility=View.INVISIBLE


            }
            else{
                txtMoreFav.visibility=View.INVISIBLE
                txtNoFav.visibility=View.VISIBLE


            }

            favAdapter.setFavtProducts(requireContext(),favProducts,4)

        }


        tvMoreOrders.setOnClickListener(View.OnClickListener {
            replaceFragment(OrdersFragment())
        })

        txtWelcomeUser.append(" ${fname} ${lname}. ")
        settingICon.setOnClickListener {
            replaceFragment(WithLoginAppSettingFragment())
        }
        shoppingCartIcon.setOnClickListener {
            replaceFragment(ShoppingCartFragment())
        }
        txtMoreFav.setOnClickListener{
            val fragment: Fragment = FavouriteFragment()
            val fragmentManager: FragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(com.example.mcommerce.R.id.frameLayout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

    private fun initComponent(view : View){
        settingICon = view.findViewById(R.id.settingICon)
        shoppingCartIcon = view.findViewById(R.id.shoppingCartIcon)
        txtWelcomeUser = view.findViewById(R.id.txtWelcomeUser)
        tvMoreOrders=view.findViewById(R.id.tvMoreOrders)
        txtMoreFav=view.findViewById(R.id.txtMoreFav)
        ordersrecycler=view.findViewById(R.id.ordersrecycler)
        tvNoOrder=view.findViewById(R.id.tvNoOrder)
        meLoginNoInternetView=view.findViewById(R.id.meLoginNoInternetView)
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onItemClickListener(draftOrderX: DraftOrderX) {
        showDeleteDialog(draftOrderX)
    }

    fun showDeleteDialog(draftOrderX: DraftOrderX) {
        val view = View.inflate(requireContext(), R.layout.remove_layout, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        view.btn_delete.setOnClickListener {
            favViewModel.deleteFavProduct(draftOrderX.id.toString())
            favViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
                if(response.isSuccessful){
                    favProducts.remove(draftOrderX)
                    favAdapter.setFavtProducts(requireContext(),favProducts,4)

                }
                else{
                    Toast.makeText(requireContext(),"Deleted failed",Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        view.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }


    override fun onOrderClickListener(order: Order) {
        communicator.goToOrderDetails(order)

    }


}