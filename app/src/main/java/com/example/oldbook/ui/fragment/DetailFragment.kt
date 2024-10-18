package com.example.oldbook.ui.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.databinding.FragmentDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var dbRef: FirebaseDatabase
    private lateinit var auth: FirebaseAuth




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()



        val name = arguments?.getString("itemname")
        val author = arguments?.getString("itemauthor")
        val image = arguments?.getString("itemimg")
        val price = arguments?.getString("itemprice")
        val type = arguments?.getString("itemtype")
        val language = arguments?.getString("itemlanguage")
        val desc = arguments?.getString("itemdescription")
        setupData(name, author, image, price, type, language, desc)

    }

    private fun setupData(
        name: String?,
        author: String?,
        image: String?,
        price: String?,
        type: String?,
        language: String?,
        desc: String?
    ) {
        binding.addtocartbtn.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference
            val userId = auth.currentUser?.uid?:""
            val cartItem = BookItemCartData(name.toString(),author.toString(), image.toString(), price.toString(),type.toString(),  1)
            dbRef.child("users").child(userId).child("cartItem").push().setValue(cartItem).addOnSuccessListener {
                Toast.makeText(requireContext(), "Item added to cart successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Item not add successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.namebooktxt.text = name
        binding.authortxt.text = author
        binding.pricetxt.text = price
        binding.languagetxt.text = language
        binding.typetxt.text = type
        binding.descriptiontxt.text = desc
        Glide.with(requireContext()).load(Uri.parse(image)).into(binding.imgbook)
    }

    private fun addItemToCart() {


//        val item = BookItemData()
//        if(!cart.itemsList.contains(item)){
//            item.quantity++
//            print(item.heading)
//            cart.itemsList.add(item)
//        }else{  // if the item is present in the cart, then just increment the quantity of the item
//            item.quantity++
//        }
//        print(cart.itemsList.size)
//        Toast.makeText(this@DetailsActivity, "Item added to cart!", Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}