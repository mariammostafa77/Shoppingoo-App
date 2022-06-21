package com.example.mcommerce.shopping_cart.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerce.ProductInfo.view.Communicator
import com.example.mcommerce.R
import com.example.mcommerce.draftModel.DraftOrder
import com.example.mcommerce.draftModel.LineItem
import com.example.mcommerce.draftModel.OrderPrices
import com.example.mcommerce.me.viewmodel.SavedSetting
import com.example.mcommerce.model.Repository
import com.example.mcommerce.network.AppClient
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModel
import com.example.mcommerce.shopping_cart.viewmodel.ShoppingCartViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ShoppingCartFragment : Fragment(), OnShoppingCartClickListener {

    lateinit var txtSubTotal: TextView
    lateinit var txtNoSDataFound: TextView
    lateinit var imgNoCartProduct: ImageView
    lateinit var address_back_icon: ImageView
    lateinit var btnProceedToCheckout: Button
    lateinit var cartProgressBar: ProgressBar
    lateinit var shoppingCartRecyclerView: RecyclerView
    lateinit var shoppingCartAdapter: ShoppingCartAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var shoppingCartViewModelFactory: ShoppingCartViewModelFactory
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    var subTotal: Double = 0.0
    lateinit var communicator: Communicator
    var lineItems: ArrayList<LineItem> = ArrayList()
    var orderPrices: ArrayList<OrderPrices> = ArrayList()
    var amount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        var userShoppingCartProducts: ArrayList<DraftOrder> = ArrayList<DraftOrder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        initComponent(view)
        shoppingCartRecyclerView.setLayoutManager(linearLayoutManager)
        shoppingCartRecyclerView.setAdapter(shoppingCartAdapter)

        address_back_icon.setOnClickListener {
            val manager: FragmentManager = activity!!.supportFragmentManager
            manager.popBackStack()
        }

        cartProgressBar.isVisible = true
        shoppingCartViewModelFactory = ShoppingCartViewModelFactory(
            Repository.getInstance(
                AppClient.getInstance(),
                requireContext()
            )
        )
        shoppingCartViewModel = ViewModelProvider(
            this,
            shoppingCartViewModelFactory
        ).get(ShoppingCartViewModel::class.java)

        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email", "")
        val note = "card"
        shoppingCartViewModel.getShoppingCardProducts()
        shoppingCartViewModel.onlineShoppingCartProduct.observe(viewLifecycleOwner) { cartProducts ->
            userShoppingCartProducts.clear()
            for (i in 0..cartProducts.size - 1) {
                if (cartProducts.get(i).note == note && cartProducts.get(i).email == email) {
                    val draftObj = DraftOrder()
                    draftObj.draft_order = cartProducts.get(i)
                    userShoppingCartProducts.add(draftObj)
                }
            }
            if (userShoppingCartProducts.isEmpty()) {
                cartProgressBar.isVisible = false
                imgNoCartProduct.visibility = View.VISIBLE
                txtNoSDataFound.visibility = View.VISIBLE
            } else {
                cartProgressBar.isVisible = false
                imgNoCartProduct.visibility = View.INVISIBLE
                txtNoSDataFound.visibility = View.INVISIBLE
            }
            shoppingCartAdapter.setUserShoppingCartProducts(
                requireContext(),
                userShoppingCartProducts
            )
            subTotal = 0.0
            for (i in 0..userShoppingCartProducts.size - 1) {
                val price = (userShoppingCartProducts[i].draft_order?.subtotal_price)?.toDouble()!!
                if (price != null) {
                    subTotal += price
                }
            }
            val amount = SavedSetting.getPrice(subTotal.toString(), requireContext())
            txtSubTotal.text = amount
        }
        btnProceedToCheckout.setOnClickListener {
            if (userShoppingCartProducts.isNotEmpty()) {
                for (i in 0..userShoppingCartProducts.size - 1) {
                    val lineItem = LineItem(
                        variant_id = userShoppingCartProducts.get(i).draft_order?.line_items?.get(0)?.variant_id,
                        quantity = userShoppingCartProducts.get(0).draft_order?.line_items?.get(0)?.quantity,
                        tax_lines = userShoppingCartProducts.get(0).draft_order?.line_items?.get(0)?.tax_lines)
                    val orderPrice = OrderPrices(tax = (userShoppingCartProducts.get(i).draft_order?.tax_lines?.get(0)?.price)
                        !!.toDouble(), subTotal = (userShoppingCartProducts.get(i).draft_order?.subtotal_price)
                        !!.toDouble(), total = (userShoppingCartProducts.get(i).draft_order?.total_price)!!.toDouble()
                    )
                    lineItems.add(lineItem)
                    orderPrices.add(orderPrice)
                }
                communicator.goToUserAddresses(lineItems, orderPrices)
            } else {
                val snack = Snackbar.make(it,"No Product Available To Continue",Snackbar.LENGTH_LONG)
                snack.show()
            }
        }

        return view
    }

    private fun initComponent(view: View) {
        address_back_icon = view.findViewById(R.id.address_back_icon)
        cartProgressBar = view.findViewById(R.id.cartProgressBar)
        txtSubTotal = view.findViewById(R.id.txtSubTotal)
        imgNoCartProduct = view.findViewById(R.id.imgNoCartProduct)
        txtNoSDataFound = view.findViewById(R.id.txtNoSDataFound)
        shoppingCartRecyclerView = view.findViewById(R.id.shoppingCartRecyclerView)
        communicator = activity as Communicator
        shoppingCartAdapter = ShoppingCartAdapter(communicator, this)
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        btnProceedToCheckout = view.findViewById(R.id.btnProceedToCheckout)

    }

    override fun onDeleteItemClickListener(draftOrder: DraftOrder) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete?")
            .setTitle("Alert!!!")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, it ->
                shoppingCartViewModel.deleteSelectedProduct(draftOrder.draft_order?.id.toString())
                shoppingCartViewModel.selectedItem.observe(viewLifecycleOwner) { response ->
                    if (response.isSuccessful) {
                        userShoppingCartProducts.remove(draftOrder)
                        shoppingCartAdapter.notifyDataSetChanged()
                        if (userShoppingCartProducts.isEmpty()) {
                            cartProgressBar.isVisible = false
                            imgNoCartProduct.visibility = View.VISIBLE
                            txtNoSDataFound.visibility = View.VISIBLE
                        } else {
                            cartProgressBar.isVisible = false
                            imgNoCartProduct.visibility = View.INVISIBLE
                            txtNoSDataFound.visibility = View.INVISIBLE
                        }
                        shoppingCartAdapter.setUserShoppingCartProducts(
                            requireContext(),
                            userShoppingCartProducts
                        )
                        subTotal = 0.0

                        for (i in 0..userShoppingCartProducts.size - 1) {
                            val price =
                                (userShoppingCartProducts[i].draft_order?.subtotal_price)?.toDouble()!!
                            if (price != null) {
                                subTotal += price
                            }
                        }
                        amount = SavedSetting.getPrice(subTotal.toString(), requireContext())
                        txtSubTotal.text = amount
                        Toast.makeText(requireContext(), "Deleted Success!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Deleted failed!! ", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, it ->
                dialog.cancel()
            }
            .show()
    }

    override fun onIncrementClickListener(draftOrder: DraftOrder) {
        val newDraftOrder = draftOrder
        newDraftOrder.draft_order?.line_items!![0].quantity = draftOrder.draft_order?.line_items!![0].quantity?.plus(1)
        shoppingCartViewModel.updateSelectedProduct(newDraftOrder.draft_order?.id.toString(), newDraftOrder)
        shoppingCartViewModel.onlineItemUpdated.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Increased Success!! ",
                    Toast.LENGTH_SHORT
                ).show()
                subTotal = 0.0
                for (i in 0..userShoppingCartProducts.size - 1) {
                    val price =
                        ((userShoppingCartProducts[i].draft_order?.line_items?.get(0)!!.price)?.toDouble())?.times(
                            (userShoppingCartProducts[i].draft_order?.line_items?.get(0)!!.quantity!!)
                        )
                    if (price != null) {
                        subTotal += price
                    }
                }
                val amount = SavedSetting.getPrice(subTotal.toString(), requireContext())
                txtSubTotal.text = amount
                getItems()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Increased Faild!! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onDecrementClickListener(draftOrder: DraftOrder) {
        val newDraftOrder = draftOrder
        newDraftOrder.draft_order?.line_items!![0].quantity =
            draftOrder.draft_order?.line_items!![0].quantity?.minus(1)
        shoppingCartViewModel.updateSelectedProduct(
            newDraftOrder.draft_order?.id.toString(),
            newDraftOrder
        )
        shoppingCartViewModel.onlineItemUpdated.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Decreased Success!! ",
                    Toast.LENGTH_SHORT
                ).show()
                subTotal = 0.0

                for (i in 0..userShoppingCartProducts.size - 1) {
                    val price =
                        ((userShoppingCartProducts[i].draft_order?.line_items?.get(0)!!.price)?.toDouble())?.times(
                            (userShoppingCartProducts[i].draft_order?.line_items?.get(0)!!.quantity!!)
                        )
                    if (price != null) {
                        subTotal += price
                    }
                }
                val amount = SavedSetting.getPrice(subTotal.toString(), requireContext())
                txtSubTotal.text = amount
                getItems()
            }
        }
    }
    fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun getItems() {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("userAuth", Context.MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("email", "")
        val note = "card"
        shoppingCartViewModel.getShoppingCardProducts()
        shoppingCartViewModel.onlineShoppingCartProduct.observe(viewLifecycleOwner) { cartProducts ->
            userShoppingCartProducts.clear()
            for (i in 0..cartProducts.size - 1) {
                if (cartProducts.get(i).note == note && cartProducts.get(i).email == email) {
                    val draftObj = DraftOrder()
                    draftObj.draft_order = cartProducts.get(i)
                    userShoppingCartProducts.add(draftObj)
                }
            }
            shoppingCartAdapter.setUserShoppingCartProducts(
                requireContext(),
                userShoppingCartProducts
            )
            subTotal = 0.0
            for (i in 0..userShoppingCartProducts.size - 1) {
                val price = (userShoppingCartProducts[i].draft_order?.subtotal_price)?.toDouble()!!
                if (price != null) {
                    subTotal += price
                }
            }
            val amount = SavedSetting.getPrice(subTotal.toString(), requireContext())
            txtSubTotal.text = amount
        }
    }

}