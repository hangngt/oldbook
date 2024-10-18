package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oldbook.R
import com.example.oldbook.adapter.BookForYouListAdapter
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.data.BookItemData
import com.example.oldbook.databinding.FragmentSearchBinding
import com.example.oldbook.ui.inter.BookItemInterface
import com.example.oldbook.ui.inter.CartInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment(), BookItemInterface, CartInterface {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var searchadapter: BookForYouListAdapter
    private lateinit var searchlist: MutableList<BookItemData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchlist = mutableListOf()
        setupbookname()
        searchdata()

        binding.listproductsearch.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
        searchadapter = BookForYouListAdapter(requireContext(), searchlist, this, this)


        binding.listproductsearch.adapter = searchadapter
    }

    private fun setupbookname() {
        searchlist.clear()
        dbRef = FirebaseDatabase.getInstance().getReference("products")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (bforyouSnap in snapshot.children) {
                        val bforyoudata = bforyouSnap.getValue(BookItemData::class.java)
                        searchlist.add(bforyoudata!!)
                    }
                    searchadapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error fetching data: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchdata() {
        binding.searchbox.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                SearchList(newText)
                return true
            }
        })
    }

    private fun SearchList(newText: String?) {
        val listbooksearch: MutableList<BookItemData> = mutableListOf()
        for (dataitem in searchlist) {
            if (dataitem.name?.lowercase()?.contains(newText!!.lowercase()) == true ||
                dataitem.author?.lowercase()?.contains(newText!!.lowercase()) == true ||
                dataitem.type?.lowercase()?.contains(newText!!.lowercase()) == true ||
                dataitem.language?.lowercase()?.contains(newText!!.lowercase()) == true) {
                listbooksearch.add(dataitem)
            }
        }
        searchadapter.searchData(listbooksearch)
    }

    // Trong fragment của bạn
    fun openDetailFragment(pos: BookItemData) {
        val detailFragment = DetailFragment()

        // Truyền dữ liệu vào fragment (nếu cần)
        val args = Bundle()
        args.putString("itemname", pos.name)
        args.putString("itemauthor", pos.author)
        args.putString("itemimg", pos.imageUrl)
        args.putString("itemtype", pos.type)
        args.putString("itemlanguage", pos.language)
        args.putString("itemprice", pos.price?.toString())
        args.putString("itemdescription", pos.description)
        detailFragment.arguments = args

        // Thực hiện thay thế fragment hiện tại bằng DetailFragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout, detailFragment)
        transaction.addToBackStack(null) // Để quay lại fragment trước đó khi ấn nút Back
        transaction.commit()
    }

    override fun OnClickBookItem(pos: BookItemData) {
        // Xử lý sự kiện khi người dùng nhấp vào một mục trong danh sách (nếu cần)
        openDetailFragment(pos)
    }

    override fun OnClickCartItem(pos: BookItemCartData) {
        // Xử lý sự kiện khi người dùng nhấp vào một mục trong giỏ hàng (nếu cần)
    }
}