package com.example.oldbook.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oldbook.R
import android.view.View
import androidx.fragment.app.Fragment
import com.example.oldbook.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intentiintro()
    }
    private fun intentiintro() {
        binding.btnintro.setOnClickListener {
//           replaceFrag(LoginFragment())
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFrag(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout_intent, fragment)
        fragmentTransaction.commit()
        binding.fragmentLayoutIntent.visibility = View.GONE
        binding.fragmentLayoutIntent.visibility = View.VISIBLE
    }

}