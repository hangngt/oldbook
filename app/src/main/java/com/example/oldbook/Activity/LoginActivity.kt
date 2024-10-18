package com.example.oldbook.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.oldbook.databinding.ActivityLoginBinding
import com.example.oldbook.ui.fragment.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseDatabase
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance()

        movesignup()
//        movehomefrag()
        login()
    }


    private fun login() {
        binding.btnlogin.setOnClickListener {
            val email = binding.editemaillg.text.toString().trim()
            val pass = binding.editpasslg.text.toString().trim()
            binding.layoutpasslg.isPasswordVisibilityToggleEnabled = true

            if (email.isEmpty() || pass.isEmpty()) {
                if (email.isEmpty()) {
                    binding.editemaillg.error = "Enter Your Email Address"
                }
                if (pass.isEmpty()) {
                    binding.editpasslg.error = "Enter Your Password"
                    binding.layoutpasslg.isPasswordVisibilityToggleEnabled = false

                }
                Toast.makeText(
                    this,
                    "Enter valid details",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                binding.editpasslg.error = "Enter valid email address"
                Toast.makeText(
                    this,
                    "Enter valid email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass.length < 6) {
                binding.editpasslg.error = "Enter password more than 6 characters"
                Toast.makeText(
                    this,
                    "Enter password more than 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                loginuser(email, pass)
            }
        }
    }

    private fun loginuser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Something went wrong, check again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnCompleteListener {
            // Đăng xuất thành công, xóa thông tin trên các trường nhập liệu
            clearEditTextFields()
        }
    }

    fun clearEditTextFields() {
        binding.editemaillg.text?.clear()
        binding.editpasslg.text?.clear()
    }

//    private fun movehomefrag() {
//        binding.btnlogin.setOnClickListener{
//            val i3 = Intent(this, HomeActivity::class.java)
//            startActivity(i3)
//        }
//    }

    private fun movesignup() {
        binding.txtsignup.setOnClickListener {
            val i1 = Intent(this, SignupActivity::class.java)
            startActivity(i1)
        }
    }
}