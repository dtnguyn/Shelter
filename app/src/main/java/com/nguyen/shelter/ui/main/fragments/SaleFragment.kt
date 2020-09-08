package com.nguyen.shelter.ui.main.fragments

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
import com.nguyen.shelter.databinding.FragmentSaleBinding
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.repo.MainRepository
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.adapters.SalePropertyAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class SaleFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var pagingAdapterSale: SalePropertyAdapter

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSaleBinding.inflate(inflater, container, false)

        val rentRecyclerView = binding.saleRecyclerview


        rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rentRecyclerView.adapter = pagingAdapterSale

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetPropertyFilter(MainRepository.SALE))

        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.salePropertyPageData.observe(viewLifecycleOwner, Observer { pagingData ->
            lifecycleScope.launch {
                pagingAdapterSale.submitData(pagingData)
            }
        })

        viewModel.salePropertyFilter.observe(viewLifecycleOwner, {filter ->
            viewModel.setStateEvent(MainStateEvent.GetSalePropertyList(filter))
        })
    }


}