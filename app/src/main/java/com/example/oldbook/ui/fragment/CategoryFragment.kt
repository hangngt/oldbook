package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oldbook.R
import com.example.oldbook.adapter.BookForYouListAdapter
import com.example.oldbook.adapter.CategoryAdapter
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.data.BookItemData
import com.google.firebase.auth.FirebaseAuth
import com.example.oldbook.databinding.FragmentCategoryBinding
import com.example.oldbook.ui.inter.BookItemInterface
import com.example.oldbook.ui.inter.CartInterface
import com.example.oldbook.ui.inter.CategoryInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryFragment : Fragment(), CategoryInterface, BookItemInterface, CartInterface {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var bookAdapter: BookForYouListAdapter
    private lateinit var dscategory: ArrayList<String>
    private lateinit var onClickCategory: CategoryInterface
    private lateinit var dsbook: ArrayList<BookItemData>
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        dsbook = arrayListOf()
        dscategory = arrayListOf()

        binding.rvcatProductsList.layoutManager = GridLayoutManager(context, 2)
        bookAdapter = BookForYouListAdapter(requireContext(),dsbook,this,this)
        binding.rvcatProductsList.adapter = bookAdapter


        binding.rvCategorylist.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvCategorylist.layoutManager = categoryLayoutManager
        categoryAdapter = CategoryAdapter(dscategory, this )
        binding.rvCategorylist.adapter = categoryAdapter
        setCategoryList()
    }

    private fun setCategoryList() {
        dscategory.clear()
        dbRef = FirebaseDatabase.getInstance().getReference("products")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uniqueCategories = HashSet<String>() // Sử dụng HashSet để lưu trữ các giá trị duy nhất

                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BookItemData::class.java)
                        val category = products?.type

                        // Kiểm tra nếu danh mục chưa tồn tại trong HashSet, thì thêm vào danh sách và HashSet
                        if (category != null && uniqueCategories.add(category)) {
                            dscategory.add(category)
                        }
                    }

                    categoryAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
    }

    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text
        dbRef = FirebaseDatabase.getInstance().getReference("products")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                dsbook.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BookItemData::class.java)

                        if (products!!.type == button.text) {
                            dsbook.add(products)
                        }

                    }

                    bookAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        })



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
        openDetailFragment(pos)
    }

    override fun OnClickCartItem(pos: BookItemCartData) {
        TODO("Not yet implemented")
    }
}