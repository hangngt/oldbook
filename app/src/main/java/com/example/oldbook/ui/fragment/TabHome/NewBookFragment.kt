package com.example.oldbook.ui.fragment.TabHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oldbook.R
import com.example.oldbook.adapter.BookForYouListAdapter
import com.example.oldbook.data.BookItemData
import com.example.oldbook.databinding.FragmentBestSellerBinding
import com.example.oldbook.databinding.FragmentNewBookBinding
import com.example.oldbook.ui.inter.BookItemInterface

class NewBookFragment : Fragment() {
    private lateinit var binding: FragmentNewBookBinding
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewBookBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listbestseller()
    }

    private fun listbestseller() {
//        val dsbookforyou = mutableListOf<BookItemData>()
//        dsbookforyou.add(BookItemData("$20",R.drawable.orangetree,"The orange tree","Literature & Fiction"))
//        dsbookforyou.add(BookItemData("$30",R.drawable.orangetree,"The orange tree","Literature & Fiction"))
//
//        var adapterrr = BookForYouListAdapter(dsbookforyou,object : BookItemInterface {
//            override fun OnClickBookItem(pos: Int) {
//                Toast.makeText(
//                    requireContext(),
//                    "Ban chon sach ${dsbookforyou[pos].bookitemname}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//        binding.newbookdemolv.adapter = adapterrr
//        binding.newbookdemolv.layoutManager = GridLayoutManager(
//            requireContext(),
//            2,
//            GridLayoutManager.HORIZONTAL,
//            false
//        )
    }

    companion object {

    }
}