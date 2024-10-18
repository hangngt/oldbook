package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oldbook.R
import com.example.oldbook.adapter.CartAdapter
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.databinding.FragmentCartBinding
import com.example.oldbook.ui.inter.CartInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment(), CartInterface {

    private lateinit var binding: FragmentCartBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var cartadapter: CartAdapter
    private lateinit var dscart: ArrayList<BookItemCartData>
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var cartquantity: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid?:""
        dbRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("cartItem")!!
        dscart = ArrayList()
        cartquantity = mutableListOf()
        binding.cartlistview.setHasFixedSize(true)
        binding.cartlistview.layoutManager = LinearLayoutManager(requireContext())
        cartadapter = CartAdapter(requireContext(), dscart, cartquantity)
        binding.cartlistview.adapter = cartadapter

//        loadCartListFromSharedPreferences()

        listcartview()
        checkoutlisten()
    }

    private fun checkoutlisten() {
        binding.checkoutbtn.setOnClickListener {
            getOrderitemDetail()
//            val checkoutfragment = CheckoutFragment()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_layout, checkoutfragment)
//            transaction.addToBackStack(null) // Để quay lại fragment trước đó khi ấn nút Back
//            transaction.commit()
        }
    }

    private fun getOrderitemDetail() {
        val OrderdbRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("cartItem")!!
        val bookname = mutableListOf<String>()
        val booktype = mutableListOf<String>()
        val bookauthor = mutableListOf<String>()
        val bookimg = mutableListOf<String>()
        val bookprice = mutableListOf<String>()
        val bookquantities = cartadapter.getUpdateQuantity()
        OrderdbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (booksnap in snapshot.children) {
                    val bookorder = booksnap.getValue(BookItemCartData::class.java)
                    bookorder?.name?.let { bookname.add(it) }
                    bookorder?.author?.let { bookauthor.add(it) }
                    bookorder?.imageUrl?.let { bookimg.add(it) }
                    bookorder?.price?.let { bookprice.add(it) }
                    bookorder?.type?.let { booktype.add(it) }
                }
                orderNow(bookname,bookauthor,bookimg, bookprice, booktype, bookquantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order Failed, Try Again!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun orderNow(
        bookname: MutableList<String>,
        bookauthor: MutableList<String>,
        bookimg: MutableList<String>,
        bookprice: MutableList<String>,
        booktype: MutableList<String>,
        bookquantities: MutableList<Int>
    ) {

        if (isAdded && context!= null) {
            val checkoutfragment = CheckoutFragment()

            // Truyền dữ liệu vào fragment (nếu cần)
            val args = Bundle()
            args.putString("bookitemname", (bookname as ArrayList<String>).toString())
            args.putString("bookitemauthor", (bookauthor as ArrayList<String>).toString())
            args.putString("bookitemimg", (bookimg as ArrayList<String>).toString())
            args.putString("bookitemprice", (bookprice as ArrayList<String>).toString())
            args.putString("bookitemtype", (booktype as ArrayList<String>).toString())
            args.putString("bookitemquantity", (bookquantities as ArrayList<Int>).toString())
            checkoutfragment.arguments = args

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_layout, checkoutfragment)
            transaction.addToBackStack(null) // Để quay lại fragment trước đó khi ấn nút Back
            transaction.commit()
        }

    }

    private fun listcartview() {
        dscart.clear()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val itemcart = dataSnapshot.getValue(BookItemCartData::class.java)
                        dscart.add(itemcart!!)
                        cartquantity.add(itemcart!!.quantity)
                    }

                    cartadapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addCartItemToCartList(bookItemCartData: BookItemCartData) {
        dscart.add(bookItemCartData)
        cartadapter.notifyDataSetChanged()
    }

//    // Lưu danh sách dscart vào SharedPreferences
//    private fun saveCartListToSharedPreferences() {
//        val gson = Gson()
//        val cartListJson = gson.toJson(dscart)
//
//        val editor = sharedPreferences.edit()
//        editor.putString("cartList", cartListJson)
//        editor.apply()
//    }
//
//    // Đọc danh sách dscart từ SharedPreferences
//    private fun loadCartListFromSharedPreferences() {
//        val cartListJson = sharedPreferences.getString("cartList", null)
//
//        if (cartListJson != null) {
//            val gson = Gson()
//            val type = object : TypeToken<ArrayList<BookItemCartData>>() {}.type
//            dscart = gson.fromJson(cartListJson, type)
//        } else {
//            dscart = ArrayList()
//        }
//    }

    companion object {
        // TODO: Thêm phương thức tạo instance nếu cần
    }

    override fun OnClickCartItem(pos: BookItemCartData) {

    }
}