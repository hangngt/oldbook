package com.example.oldbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.databinding.ItemLayoutBookCartBinding
import com.example.oldbook.databinding.LayoutBookItemBinding
import com.example.oldbook.ui.inter.BookItemInterface
import com.example.oldbook.ui.inter.CartInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter (
    val context: Context,
    var dscart: MutableList<BookItemCartData>,
    private var cartquantity: MutableList<Int>
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemLayoutBookCartBinding):
        RecyclerView.ViewHolder(binding.root)

    private val auth = FirebaseAuth.getInstance()
    init {
        val dbRef = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNum = dscart.size

        itemquantity = IntArray(cartItemNum){1}
        cartItemRef = dbRef.reference.child("users").child(userId).child("cartItem")!!
    }

//    private val itemquantity = IntArray(dscart.size){1}

    companion object{
        private var itemquantity: IntArray = intArrayOf()
        private lateinit var cartItemRef: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemLayoutBookCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Log.d("CartAdapter", "dscart size: ${dscart.size}, cartquantity size: ${cartquantity.size}, position: $position")
        val binding = holder.binding
//        val quantity = itemquantity[position]

        binding.itemtitlecart.text = dscart[position].name
        binding.itempricecart.text = dscart[position].price.toString()

        Glide.with(context).load( dscart[position].imageUrl).into( binding.imgbookitemcart)
        // Cập nhật số lượng và hiển thị
        binding.quantityitemcart.text = cartquantity[position].toString()


// Xử lý sự kiện trừ
        binding.minusitemcart.setOnClickListener {
            if (cartquantity[position] > 1) {
                cartquantity[position]--
                binding.quantityitemcart.text = cartquantity[position].toString()
                getUniqueKeyAtPosition(position) { uniqueKey ->
                    if (uniqueKey != null) {
                        cartItemRef.child(uniqueKey).child("quantity")
                            .setValue(cartquantity[position])
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Quantity updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Failed to update quantity",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
        }

// Xử lý sự kiện thêm
        binding.additemcart.setOnClickListener {
            if (cartquantity[position] < 40) {
                cartquantity[position]++
                binding.quantityitemcart.text = cartquantity[position].toString()
                // Cập nhật giá trị cartquantity trong Firebase
                getUniqueKeyAtPosition(position) { uniqueKey ->
                    if (uniqueKey != null) {
                        cartItemRef.child(uniqueKey).child("quantity")
                            .setValue(cartquantity[position])
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Quantity updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Failed to update quantity",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
        }
//        xoa
        binding.itemdelcart.setOnClickListener {
//            val itempos = holder.adapterPosition
//            if (itempos != RecyclerView.NO_POSITION) {
//                dscart.removeAt(itempos)
//                notifyItemRemoved(itempos)
//                notifyItemRangeRemoved(itempos, dscart.size)
//            }
            getUniqueKeyAtPosition(position) {uniquekey ->
                removeItem(position, uniquekey)
            }
        }

    }

    fun getUpdateQuantity(): MutableList<Int> {
        val quantity = mutableListOf<Int>()
        quantity.addAll(cartquantity)
        return quantity
    }

//    private fun removeItem(position: Int, uniquekey: String?) {
//        if (uniquekey != null) {
//            cartItemRef.child(uniquekey).removeValue().addOnSuccessListener {
//                dscart.removeAt(position)
//                cartquantity.removeAt(position)
//
////                update itemquantities
//                itemquantity = itemquantity.filterIndexed{ index, i -> index!= position }.toIntArray()
//
//                notifyItemRemoved(position)
//                notifyItemRangeRemoved(position, dscart.size)
//                Toast.makeText(
//                    context,
//                    "Delete item successfull",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }.addOnFailureListener {
//                Toast.makeText(
//                    context,
//                    "Failed to Delete",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    private fun removeItem(position: Int, uniquekey: String?) {
        if (uniquekey != null) {
            cartItemRef.child(uniquekey).removeValue().addOnSuccessListener {
                dscart.removeAt(position)
                cartquantity.removeAt(position)
                itemquantity = itemquantity.filterIndexed { index, _ -> index != position }.toIntArray()

                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, dscart.size)
                Toast.makeText(
                    context,
                    "Delete item successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Failed to delete",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getUniqueKeyAtPosition(position: Int, onComplete: (String?) -> Unit) {
        cartItemRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniquekey: String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == position) {
                        uniquekey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniquekey)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int {
        return dscart.size
    }
}