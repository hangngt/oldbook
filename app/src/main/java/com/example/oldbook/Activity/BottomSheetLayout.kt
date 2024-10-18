package com.example.oldbook.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oldbook.R
import com.example.oldbook.databinding.BottomsheetlayoutBinding
import com.example.oldbook.ui.fragment.AccountFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

open class BottomSheetLayout(private val activity: HomeActivity): AppCompatActivity() {

    private lateinit var binding: BottomsheetlayoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var uid: String

    init {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    open fun showBottomDialog() {
        val inflater = LayoutInflater.from(activity)
        val view: View = inflater.inflate(R.layout.bottomsheetlayout, null)
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(view)

        dialog.findViewById<LinearLayout>(R.id.informationlayout)?.setOnClickListener {
           activity.replaceFragment(AccountFragment())
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.logoutlayout)?.setOnClickListener {
            auth.signOut()
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        dialog.show()
    }
}