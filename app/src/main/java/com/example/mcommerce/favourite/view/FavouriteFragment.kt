package com.example.mcommerce.favourite.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import android.widget.Toast
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
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.network.CheckInternetConnectionFirstTime
import com.example.mcommerce.network.InternetConnectionChecker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.remove_layout.view.*


class FavouriteFragment : Fragment(),FavouriteOnClickLisner {
    lateinit var favRecyclerView: RecyclerView
    lateinit var favAdapter: FavProductsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var favViewModelFactory : ProductInfoViewModelFactory
    lateinit var favViewModel: ProductInfoViewModel
    lateinit var communicator: Communicator
    lateinit var noDataImage:ImageView
    lateinit var noInternet:ImageView
    lateinit var txtNoData:TextView
    lateinit var favProgressbar:ProgressBar
    lateinit var favBackImg:ImageView
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    var favProducts:ArrayList<DraftOrderX> = ArrayList<DraftOrderX>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view=inflater.inflate(R.layout.fragment_favourite, container, false)
        communicator = activity as Communicator
        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        noDataImage=view.findViewById(R.id.noDataImg)
        txtNoData=view.findViewById(R.id.txtNoData)
        favBackImg=view.findViewById(R.id.favBackImg)
        favProgressbar=view.findViewById(R.id.favProgressBar)
        Log.i("FavArray","test Fav: ")
        favAdapter= FavProductsAdapter(this,communicator)
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        favRecyclerView.setLayoutManager(linearLayoutManager)
        favRecyclerView.setAdapter(favAdapter)
        noInternet=view.findViewById(R.id.noInternetImg)
        favViewModelFactory = ProductInfoViewModelFactory(Repository.getInstance(AppClient.getInstance(), requireContext()))
        favViewModel = ViewModelProvider(this, favViewModelFactory).get(ProductInfoViewModel::class.java)
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email","")
        val note = "fav"
        if(CheckInternetConnectionFirstTime.checkForInternet(requireContext())) {

            favProgressbar.visibility = View.VISIBLE
            noInternet.visibility = View.INVISIBLE
            favViewModel.getFavProducts()

        }else{
            noInternet.visibility = View.VISIBLE
        }
        internetConnectionChecker = InternetConnectionChecker(requireContext())
        internetConnectionChecker.observe(this,{ isConnected ->
            if (isConnected){

                favViewModel.getFavProducts()
                noInternet.visibility = View.INVISIBLE

            }
        else{
                var snake = Snackbar.make(view, "Ops! You Lost internet connection!!!", Snackbar.LENGTH_LONG)
                snake.show()
        }
        })

        favViewModel.onlineFavProduct.observe(viewLifecycleOwner) { allFavProducts ->
            favProducts.clear()
            for (i in 0..allFavProducts.size-1){
                if(allFavProducts.get(i).note == note && allFavProducts.get(i).email == email){
                    favProducts.add(allFavProducts.get(i))

                }

            }
            if(favProducts.isEmpty()){
                noDataImage.visibility=View.VISIBLE
                txtNoData.visibility=View.VISIBLE
                favProgressbar.visibility = View.INVISIBLE
            }
            else{
                noDataImage.visibility=View.INVISIBLE
                txtNoData.visibility=View.INVISIBLE
                favProgressbar.visibility = View.INVISIBLE
            }

            favAdapter.setFavtProducts(requireContext(),favProducts,favProducts.size)

        }
        favBackImg.setOnClickListener {
            val manager: FragmentManager = activity!!.supportFragmentManager
            val trans: FragmentTransaction = manager.beginTransaction()
            trans.remove(this)
            trans.commit()
            manager.popBackStack()

        }
        return view
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
                if (response.isSuccessful) {
                    favProducts.remove(draftOrderX)
                    if (favProducts.isEmpty()) {
                        noDataImage.visibility = View.VISIBLE
                        txtNoData.visibility = View.VISIBLE
                    } else {
                        noDataImage.visibility = View.INVISIBLE
                        txtNoData.visibility = View.INVISIBLE
                    }

                    favAdapter.setFavtProducts(requireContext(),
                        favProducts,
                        favProducts.size)

                } else {
                    Toast.makeText(requireContext(),
                        "Deleted failed",
                        Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
        view.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }


}