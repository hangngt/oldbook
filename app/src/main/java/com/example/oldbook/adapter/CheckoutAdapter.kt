package com.example.oldbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.databinding.ItemLayoutBookCartBinding
import com.example.oldbook.databinding.LayoutItemCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckoutAdapter ( val context: Context,
                        var dscart: MutableList<BookItemCartData>
): RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    inner class CheckoutViewHolder(val binding: LayoutItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val auth = FirebaseAuth.getInstance()

    init {
        val dbRef = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""

        cartItemRef = dbRef.reference.child("users").child(userId).child("cartItem")!!
    }

//    private val itemquantity = IntArray(dscart.size){1}

    companion object {
        private lateinit var cartItemRef: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val binding =
            LayoutItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val binding = holder.binding

        binding.itemtitlecko.text = dscart[position].name
        binding.itempricecko.text = dscart[position].price.toString()
        binding.itemquantity.text = dscart[position].quantity.toString()

        Glide.with(context).load(dscart[position].imageUrl).into(binding.imgbookitemcko)
    }


    override fun getItemCount(): Int {
        return dscart.size
    }
}