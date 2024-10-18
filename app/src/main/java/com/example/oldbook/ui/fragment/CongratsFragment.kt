package com.example.oldbook.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oldbook.R
import com.example.oldbook.databinding.FragmentCongratsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gohomelisten()
    }

    private fun gohomelisten() {
        binding.gohomebtn.setOnClickListener {
            dismiss()
            val homefragment = HomeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_layout, homefragment)
            transaction.addToBackStack(null) // Để quay lại fragment trước đó khi ấn nút Back
            transaction.commit()
        }
    }

    companion object {

    }
}