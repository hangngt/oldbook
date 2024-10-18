package com.example.oldbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.data.BookItemData
import com.example.oldbook.databinding.LayoutBookItemBinding
import com.example.oldbook.ui.inter.BookItemInterface
import com.example.oldbook.ui.inter.CartInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BookForYouListAdapter(
    val context: Context,
    var dsbookforyou: MutableList<BookItemData>,
    var OnClickBookItem: BookItemInterface,
    var OnClickCartItem: CartInterface
): RecyclerView.Adapter<BookForYouListAdapter.BookForYouViewHolder>() {
    private val auth = FirebaseAuth.getInstance()

    inner class BookForYouViewHolder(val binding: LayoutBookItemBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BookForYouViewHolder{
        val binding = LayoutBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookForYouViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookForYouViewHolder, position: Int) {
        val dsbook = dsbookforyou[position]
        val binding = holder.binding

        binding.txtbookitemname.text = dsbook.name
        binding.txtbookitemtype.text = dsbook.type
        binding.txtpriceitem.text = "$" + dsbook.price.toString()

        Glide.with(context).load( dsbook.imageUrl).into( binding.imgbookitem)

        holder.itemView.setOnClickListener {
            OnClickBookItem.OnClickBookItem(dsbookforyou[position])
        }

        binding.addtocartbtn.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference
            val userId = auth.currentUser?.uid ?: ""
            val cartItem = BookItemCartData(
                dsbook.name.toString(),
                dsbook.author.toString(),
                dsbook.imageUrl.toString(),
                dsbook.price.toString(),
                dsbook.type.toString(),
                1
            )
            dbRef.child("users").child(userId).child("cartItem").push()
                .setValue(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "Item added to cart successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Item not added successfully!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun searchData(searchlist : MutableList<BookItemData>) {
        dsbookforyou = searchlist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dsbookforyou.size
    }
}