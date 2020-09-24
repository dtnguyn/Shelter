package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentSaleBinding
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.repo.MainRepository
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


    lateinit var pagingAdapterSale: SalePropertyAdapter

    @Inject
    lateinit var cacheMapper: PropertyCacheMapper

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSaleBinding.inflate(inflater, container, false)

        val rentRecyclerView = binding.saleRecyclerview


        rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        pagingAdapterSale = SalePropertyAdapter(cacheMapper){bundle, transformationLayout ->
            val extras = FragmentNavigatorExtras(transformationLayout to bundle.getString("id")!!)
            findNavController().navigate(R.id.detail_fragment, bundle, null, extras)
        }
        rentRecyclerView.adapter = pagingAdapterSale

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetPropertyFilter(MainRepository.SALE))

        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.salePropertyPageData.observe(viewLifecycleOwner, { pagingData ->
            lifecycleScope.launch {
                pagingAdapterSale.submitData(pagingData)
            }
        })

        viewModel.salePropertyFilter.observe(viewLifecycleOwner, {filter ->
            viewModel.setStateEvent(MainStateEvent.GetSalePropertyList(filter))
        })
    }


}