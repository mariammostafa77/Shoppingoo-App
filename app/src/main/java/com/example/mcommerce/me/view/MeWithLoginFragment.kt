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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrderX
import com.example.mcommerce.favourite.view.FavProductsAdapter
import com.example.mcommerce.favourite.view.FavouriteFragment
import com.example.mcommerce.favourite.view.FavouriteOnClickLisner
import com.example.mcommerce.favourite.viewModel.FavViewModel
import com.example.mcommerce.favourite.viewModel.FavViewModelFactory
import com.example.mcommerce.me.view.setting.AppSettingFragment
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.orders.view.OrdersFragment
import com.example.mcommerce.shopping_cart.view.ShoppingCartFragment

class MeWithLogin : Fragment(), FavouriteOnClickLisner {

    lateinit var settingICon: ImageView
    lateinit var shoppingCartIcon : ImageView
    lateinit var txtWelcomeUser : TextView
    lateinit var txtMoreFav:TextView
    lateinit var tvMoreOrders : TextView
    lateinit var ordersrecycler:RecyclerView
    lateinit var favRecyclerView: RecyclerView
    lateinit var favAdapter: FavProductsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var favViewModelFactory : FavViewModelFactory
    lateinit var favViewModel: FavViewModel
    lateinit var communicator: Communicator
    var favProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()
    var userName : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_me_with_login, container, false)
        initComponent(view)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val fname: String? = sharedPreferences.getString("fname","")
        val lname: String? = sharedPreferences.getString("lname","")
        //favourite arrayList
        communicator = activity as Communicator
        favRecyclerView = view.findViewById(R.id.wishListRecyclerview)

        favAdapter= FavProductsAdapter(this,communicator)
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        favRecyclerView.setLayoutManager(linearLayoutManager)
        favRecyclerView.setAdapter(favAdapter)

        favViewModelFactory = FavViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        favViewModel = ViewModelProvider(this, favViewModelFactory).get(FavViewModel::class.java)

        val email: String? = sharedPreferences.getString("email","")
        val note = "fav"
        favViewModel.getFavProducts()
        favViewModel.onlineFavProduct.observe(viewLifecycleOwner) { allFavProducts ->

            for (i in 0..allFavProducts.size-1){
                if(allFavProducts.get(i).note == note && allFavProducts.get(i).email == email){

                    favProducts.add(allFavProducts.get(i))

                }

            }

            favAdapter.setFavtProducts(requireContext(),favProducts,4)

        }


        tvMoreOrders.setOnClickListener(View.OnClickListener {
            replaceFragment(OrdersFragment())
        })

        txtWelcomeUser.append(" ${fname} ${lname}. ")
        settingICon.setOnClickListener {
            replaceFragment(AppSettingFragment())
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
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
    override fun onItemClickListener(draftOrderX: DraftOrderX) {
        Toast.makeText(requireContext(),"clicked",Toast.LENGTH_LONG).show()
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete?")
            .setTitle("Remove")
            .setCancelable(false)
            .setPositiveButton("Yes"){ dialog , it ->
                favViewModel.deleteSelectedProduct(draftOrderX.id.toString())
                favViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
                    if(response.isSuccessful){
                        favProducts.remove(draftOrderX)
                        favAdapter.setFavtProducts(requireContext(),favProducts,4)

                        Toast.makeText(requireContext(),"Deleted Success!!!: "+response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Deleted failed: "+response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("No"){ dialog , it ->
                dialog.cancel()
            }
            .show()
    }


}