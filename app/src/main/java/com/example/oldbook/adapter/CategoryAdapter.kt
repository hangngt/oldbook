package com.example.oldbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oldbook.databinding.FragmentCategoryBinding
import com.example.oldbook.databinding.LayoutItemCategoryBinding
import com.example.oldbook.ui.inter.CategoryInterface

class CategoryAdapter (var list: ArrayList<String>, var onClickCategory: CategoryInterface): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            LayoutItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val binding = LayoutItemCategoryBinding.bind(holder.itemView)
        binding.btnItemCategory.text = list[position]

        binding.btnItemCategory.setOnClickListener {
            onClickCategory.onClickCategory(binding.btnItemCategory)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}