package com.example.oldbook.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.oldbook.data.Users
import com.example.oldbook.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        movelogin()
        Signup()
    }

    private fun movelogin() {
        binding.txtlogin.setOnClickListener {
            val i2 = Intent(this, LoginActivity::class.java)
            startActivity(i2)
        }
    }


    private fun Signup() {
        binding.btnsignup.setOnClickListener {
            val name = binding.editnamesu.text.toString().trim()
            val email = binding.editmailsu.text.toString().trim()
            val phone = binding.editphonesu.text.toString().trim()
            val pass = binding.editpasssu.text.toString().trim()
            val confpass = binding.editpassconfsu.text.toString().trim()
            binding.layoutpasssu.isPasswordVisibilityToggleEnabled = true
            binding.layoutpassconfsu.isPasswordVisibilityToggleEnabled = true


            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty() || confpass.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
                binding.layoutpasssu.isPasswordVisibilityToggleEnabled = false
                binding.layoutpassconfsu.isPasswordVisibilityToggleEnabled = false
            } else if (!email.matches(emailPattern.toRegex())) {
                binding.editmailsu.error = "Enter valid email address"
                Toast.makeText(
                    this,
                    "Enter valid email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (phone.length != 10) {
                binding.editphonesu.error = "Enter valid phone number"
                Toast.makeText(
                    this,
                    "Enter valid phone number",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass.length < 6) {
                binding.editpasssu.error = "Enter password more than 6 characters"
                Toast.makeText(
                    this,
                    "Enter password more than 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }  else if (pass != confpass) {
                binding.editpassconfsu.error = "Password not matched, try again"
                Toast.makeText(
                    this,
                    "Password not matched, try again",
                    Toast.LENGTH_SHORT
                ).show()
            }  else {
                createUser(name, email, phone, pass)
            }
        }
    }

    private fun createUser(name: String, email: String,phone: String,  password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val databaseRef = dbRef.reference.child("users").child(auth.currentUser!!.uid)
                    val user: Users = Users(name, email,password, phone)

                    databaseRef.setValue(user).addOnCompleteListener { task->
                        if (task.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Something went wrong, try again!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}