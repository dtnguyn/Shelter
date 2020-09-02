package com.nguyen.shelter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.databinding.FragmentRentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RentFragment: Fragment() {

    private lateinit var binding: FragmentRentBinding

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var pagingAdapterRent: RentPropertyAdapter

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentBinding.inflate(inflater, container, false)

        val rentRecyclerView = binding.rentRecyclerview


        rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rentRecyclerView.adapter = pagingAdapterRent


        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetRentPropertyList)

        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.rentPropertyPageData.observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                pagingAdapterRent.submitData(pagingData)
            }

        })
    }

}