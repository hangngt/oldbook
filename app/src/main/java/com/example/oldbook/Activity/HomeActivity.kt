package com.example.oldbook.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.oldbook.databinding.ActivityHomeBinding
import com.example.oldbook.R
import com.example.oldbook.ui.fragment.CartFragment
import com.example.oldbook.ui.fragment.CategoryFragment
import com.example.oldbook.ui.fragment.HomeFragment
import com.example.oldbook.ui.fragment.SearchFragment


open class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movehome()
        clickon()
    }

    private fun clickon() {
        binding.btnsearch.setOnClickListener {
            replaceFragment(SearchFragment())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_bottom, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun movehome() {
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                }
                R.id.bottom_category ->{
                    replaceFragment(CategoryFragment())
                }
                R.id.bottom_cart ->{
                    replaceFragment(CartFragment())
                }
                R.id.bottom_account -> {
                    BottomSheetLayout(this).showBottomDialog()
                }
            }
            true
        }

    }
    open fun replaceFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_layout,fragment)
            .commit()
    }



}