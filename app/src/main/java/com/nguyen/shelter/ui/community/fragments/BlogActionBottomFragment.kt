package com.nguyen.shelter.ui.community.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyen.shelter.databinding.BlogActionBottomSheetBinding

class BlogActionBottomFragment(private val isOwner: Boolean, private val onActionClick: (String) -> Unit): BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BlogActionBottomSheetBinding.inflate(inflater, container, false)

        println("debug: isOwner $isOwner")

        binding.isOwner = isOwner

        binding.editButton.setOnClickListener {
            onActionClick.invoke("edit")
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            onActionClick.invoke("delete")
            dismiss()
        }

        binding.reportButton.setOnClickListener {
            onActionClick.invoke("report")
            dismiss()
        }

        binding.removeButton.setOnClickListener {
            onActionClick.invoke("remove")
            dismiss()
        }


        return binding.root
    }

}