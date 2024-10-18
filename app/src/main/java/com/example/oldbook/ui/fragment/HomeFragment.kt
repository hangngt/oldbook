package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.oldbook.R
import com.example.oldbook.adapter.AuthorListAdapter
import com.example.oldbook.adapter.BookForYouListAdapter
import com.example.oldbook.data.AuthorData
import com.example.oldbook.data.BookItemCartData
import com.example.oldbook.data.BookItemData
import com.example.oldbook.databinding.FragmentHomeBinding
import com.example.oldbook.ui.inter.AuthorInterface
import com.example.oldbook.ui.inter.BookItemInterface
import com.example.oldbook.ui.inter.CartInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AuthorInterface, BookItemInterface, CartInterface {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var OnClickAuthor: AuthorInterface
    private lateinit var auadapter: AuthorListAdapter
    private lateinit var bforyouadapter: BookForYouListAdapter
    private lateinit var OnClickBookItem: BookItemInterface
    private lateinit var dsbookforyou: MutableList<BookItemData>
    private lateinit var dsauthor: ArrayList<AuthorData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabBookDemo // Đảm bảo rằng đã khởi tạo tabLayout
        return binding.root



    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
//        listnewselll()
        slidebanner()
        dsauthor = arrayListOf()
        dsbookforyou = mutableListOf()



//      author
        binding.listauthordemo.layoutManager = GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.listauthordemo.setHasFixedSize(true)
        auadapter = AuthorListAdapter(requireContext(), dsauthor, this)
        binding.listauthordemo.adapter = auadapter
        listauthor()


//        for you list book
        binding.listforyoudemo.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.listforyoudemo.setHasFixedSize(true)
        bforyouadapter = BookForYouListAdapter(requireContext(),dsbookforyou,this,this)
        binding.listforyoudemo.adapter = bforyouadapter
        listbookforyou()
    }

    private fun listnewselll() {
//        val adapterv = ViewPageAdapter(childFragmentManager,lifecycle)
//        binding.pagertest.adapter = adapterv
//        TabLayoutMediator(binding.tabBookDemo, binding.pagertest){tab, pos->
//            when(pos) {
//                0->{
//                    tab.text = "Tab 1"
//                }
//                1->{
//                    tab.text = "Tab 2"
//                }
//            }
//        }.attach()
    }


    private fun listbookforyou() {
        dsbookforyou.clear()
        dbRef = FirebaseDatabase.getInstance().getReference("products")
        dbRef.limitToFirst(6).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (bforyouSnap in snapshot.children) {
                        val bforyoudata = bforyouSnap.getValue(BookItemData::class.java)
                        dsbookforyou.add(bforyoudata!!)
                    }
                    bforyouadapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error fetching data: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun listauthor() {
        dsauthor.clear()
        dbRef = FirebaseDatabase.getInstance().getReference("author")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (autSnap in snapshot.children) {
                        val autdata = autSnap.getValue(AuthorData::class.java)
                        dsauthor.add(autdata!!)
                    }
                    auadapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun slidebanner() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))

        val imageSlider = binding.imageslider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMess = "Selected Image $position"
                Toast.makeText(requireContext(), itemMess, Toast.LENGTH_SHORT).show()
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


    companion object{
    }

    override fun OnClickAuthor(pos: AuthorData) {
        TODO("Not yet implemented")
    }

    override fun OnClickBookItem(pos: BookItemData) {
        openDetailFragment(pos)
    }

    override fun OnClickCartItem(pos: BookItemCartData) {
        TODO("Not yet implemented")
    }

}

