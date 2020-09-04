package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentFilterBinding
import com.nguyen.shelter.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

class FilterFragment : Fragment() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity?)?.hideActionBar()
    }


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFilterBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
            (activity as MainActivity?)?.showActionBar()
        }


        return binding.root
    }

}