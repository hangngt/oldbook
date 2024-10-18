package com.example.oldbook.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.oldbook.databinding.FragmentBestSellerBinding
import com.example.oldbook.databinding.FragmentNewBookBinding
import com.example.oldbook.ui.fragment.TabHome.BestSellerFragment
import com.example.oldbook.ui.fragment.TabHome.NewBookFragment

class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                BestSellerFragment()
            }

            else -> {
                NewBookFragment()
            }
        }
    }
}