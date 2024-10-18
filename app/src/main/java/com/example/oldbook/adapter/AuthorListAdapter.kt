package com.example.oldbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.oldbook.data.AuthorData
import com.example.oldbook.databinding.LayoutItemAuthorBinding
import com.example.oldbook.ui.inter.AuthorInterface

class AuthorListAdapter(
    val context: Context,
    private val dsauthor: MutableList<AuthorData>,
    private val onClickAuthor: AuthorInterface
) : RecyclerView.Adapter<AuthorListAdapter.AuthorViewHolder>() {

    inner class AuthorViewHolder(val binding: LayoutItemAuthorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val binding =
            LayoutItemAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = dsauthor[position]
        val binding = holder.binding

        binding.txtauthorname.text = author.name

        Glide.with(context).load( author.imageUrl).into( binding.imgauthor)

        holder.itemView.setOnClickListener {
            onClickAuthor.OnClickAuthor(author)
        }
    }

    override fun getItemCount(): Int {
        return dsauthor.size
    }
}