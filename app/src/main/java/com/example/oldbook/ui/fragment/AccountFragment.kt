package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.oldbook.data.BookItemData
import com.example.oldbook.data.Users
import com.example.oldbook.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.type.Date
import java.io.File
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.oldbook.R
import java.util.Random

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: FirebaseStorage
    private lateinit var uid: String
    private lateinit var user: Users
    private lateinit var editTextList: List<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextList = listOf(
            binding.nameprofiletxt,
            binding.phonetxt,
            binding.emailtxt,
            binding.usernametxt,
            binding.addresstxt
        )

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("users")
        if (!uid.isEmpty()) {
            getuserdata()
        }
        setupEditTextListeners()
    }

    private fun setupEditTextListeners() {
        editTextList.forEach { editText ->
            editText.setOnClickListener {
                binding.updatebtn.visibility = View.VISIBLE
                setupdate()
            }
        }
    }

    private fun setupdate() {
        binding.updatebtn.setOnClickListener {
            val username = binding.usernametxt.text.toString()
            val name = binding.nameprofiletxt.text.toString()
            val phone = binding.phonetxt.text.toString()
            val address = binding.addresstxt.text.toString()
            val email = binding.emailtxt.text.toString()

            val user = Users(name, email, phone, address, username)
            if (uid != null) {
                dbRef.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Update profile successfull",
                            Toast.LENGTH_SHORT
                        ).show()
                        auth.currentUser?.updateEmail(email)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun getuserdata() {
        dbRef.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                   val userdata = snapshot.getValue(Users::class.java)
                   if (userdata != null) {
                       val random = Random()
                       val randomNumber = random.nextInt(20) + 1
                       val username = "User_$randomNumber"
                       editTextList.forEach { editText ->
                           when (editText) {
                               binding.nameprofiletxt -> editText.setText(userdata.name)
                               binding.phonetxt -> editText.setText(userdata.phone)
                               binding.emailtxt -> editText.setText(userdata.email)
//                               binding.usernametxt!! -> editText.setText(username)
                               binding.usernametxt!! -> {
                                   if (userdata.username != null) {
                                       editText.setText(userdata.username)
                                   }
                                   else {
                                       editText.setText(username)
                                   }
                               }
                               binding.addresstxt!! -> {
                                   if (userdata.address != null) {
                                       editText.setText(userdata.address)
                                   }
                                   else {
                                       editText.setText("Address")
                                   }
                               }
                           }
                       }
                   }
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