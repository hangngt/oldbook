package com.example.oldbook.ui.fragment

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oldbook.adapter.CheckoutAdapter
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.data.OrderData
import com.example.oldbook.data.Users
import com.example.oldbook.databinding.FragmentCheckoutBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.math.BigDecimal
import java.math.RoundingMode


class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var ckoadapter: CheckoutAdapter
    private lateinit var dscart: ArrayList<BookItemCartData>
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private var address: String = ""
    private var username: String = ""
    private var userphone: String = ""
    private var product: MutableList<BookItemCartData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    var name = arguments?.getStringArrayList("bookitemname")
    var author = arguments?.getStringArrayList("bookitemauthor")
    var image = arguments?.getStringArrayList("bookitemimg")
    var price = arguments?.getStringArrayList("bookitemprice")
    var type = arguments?.getStringArrayList("bookitemtype")
    var quantity = arguments?.getStringArrayList("bookitemquantity")

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        dbRef = FirebaseDatabase.getInstance().reference
        dscart = ArrayList()
        binding.cartlistview.setHasFixedSize(true)
        binding.cartlistview.layoutManager = LinearLayoutManager(requireContext())
        ckoadapter = CheckoutAdapter(requireContext(), dscart)
        binding.cartlistview.adapter = ckoadapter



            // Đảm bảo rằng price và quantity không null trước khi gọi getTotalAmount()
            if (price != null && quantity != null) {
                getTotalAmount { total ->
                    // Cập nhật UI ở đây
                    binding.subtotaltxt.text = total.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
                    val delivery = 15
                    val totalAmount = total+delivery
                    binding.totaltxt.text = totalAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
                }

            } else {
                // Xử lý trường hợp price hoặc quantity null
                Toast.makeText(context, "Price or Quantity data is missing.", Toast.LENGTH_SHORT).show()
            }

            listcheckoutview()

            if (!uid.isEmpty()) {
                setaddressuser()
            }

            ordernow()
        }

private fun getTotalAmount(callback: (Double) -> Unit) {
    val userId = auth.currentUser?.uid ?: ""
    dbRef.child("users").child(userId).child("cartItem").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var total = 0.0
            snapshot.children.forEach { dataSnapshot ->
                val price = dataSnapshot.child("price").getValue(String::class.java)?.toDouble() ?: 0.0
                val quantity = dataSnapshot.child("quantity").getValue(Int::class.java)?.toDouble() ?: 0.0
                total += price * quantity
            }
            callback(total)
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
        }
    })
}




    private fun ordernow() {
        binding.placeorderbtn.setOnClickListener {
//            address = binding.addresstxt.text.toString()
            if (address.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Please Enter The Address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                placeOrder()
            }

        }
    }


    private fun placeOrder() {

        val userId = auth.currentUser?.uid ?: ""
        val time = Timestamp.now()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timedate = dateFormat.format(time.toDate())
        val itemPushKey = dbRef.child("orderdetails").push().key
//        val totalAmount = binding.totaltxt.text.toString().toDouble()
        val totalPriceString =  binding.totaltxt.text.toString()
        val totalPriceDouble = if (totalPriceString.isNotBlank()) {
            totalPriceString.toDouble()
        } else {
            0.0
        }

        val orderdetails = OrderData(userId, username, address, userphone, totalPriceDouble, timedate, dscart.size, product)
        val orderRef = dbRef.child("orderdetails").child(itemPushKey!!)
        orderRef.setValue(orderdetails).addOnSuccessListener {
            val bottomsheetdialog = CongratsFragment()
            bottomsheetdialog.show(childFragmentManager, "Test")
            removeIteminCart()
        }
    }

    private fun removeIteminCart() {
        val userId = auth.currentUser?.uid ?: ""
        val cartItemRef = dbRef.child("users").child(userId).child("cartItem")
        cartItemRef.removeValue()
    }

    private fun setaddressuser() {
        val user = auth.currentUser
        if (user != null) {
            dbRef.child("users").child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userdata = snapshot.getValue(Users::class.java)
                        if (userdata != null) {
                            binding.addresstxt.setText(userdata.address)
                            address = userdata.address.toString()
                            userphone = userdata.phone.toString()
                            username = userdata.name.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun listcheckoutview() {
        dscart.clear()
        quantity?.clear() // Thêm dòng này để xóa các giá trị cũ trước khi thêm giá trị mới
        val userId = auth.currentUser?.uid ?: ""
        dbRef.child("users").child(userId).child("cartItem")!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dscart.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val itemcart = dataSnapshot.getValue(BookItemCartData::class.java)
                        dscart.add(itemcart!!)
//                        itemcart!!.name?.let { product.add("product name: " + it) }
//                        itemcart!!.author?.let { product.add("product author: " + it) }
                        product.add(itemcart!!)
                        quantity?.add(itemcart!!.quantity.toString())
                    }

                    ckoadapter.notifyDataSetChanged()
                }

                // Calculate total amount after retrieving data
                getTotalAmount { total ->
                    // Update UI here
                    binding.subtotaltxt.text = "$" + total.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
                    val delivery = 15
                    val totalAmount = total+delivery
                    binding.totaltxt.text = totalAmount.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {

    }
}