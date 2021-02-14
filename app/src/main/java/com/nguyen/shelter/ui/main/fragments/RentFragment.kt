package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentRentBinding
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.repo.MainRepository.Companion.RENT
import com.nguyen.shelter.ui.main.adapters.RentPropertyAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import com.nguyen.shelter.util.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class RentFragment: Fragment() {

    private lateinit var binding: FragmentRentBinding

    private val viewModel: MainViewModel by viewModels()


    private lateinit var pagingAdapterRent: RentPropertyAdapter

    private lateinit var connectionLiveData: ConnectionLiveData

    @Inject
    lateinit var cacheMapper: PropertyCacheMapper



    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentBinding.inflate(inflater, container, false)
        binding.isLoading = false
        createAdapter()

        val rentRecyclerView = binding.rentRecyclerview

        rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rentRecyclerView.adapter = pagingAdapterRent

        viewModel.setStateEvent(MainStateEvent.GetPropertyFilter(RENT))

        connectionLiveData = ConnectionLiveData(requireContext())


        subscribeObservers(binding)

        return binding.root
    }

    @ExperimentalPagingApi
    private fun createAdapter(){

        pagingAdapterRent = RentPropertyAdapter(cacheMapper,
        detailOnClick = {bundle, transformationLayout ->
            val extras = FragmentNavigatorExtras(transformationLayout to bundle.getParcelable<Property>("prop")!!.id)
            findNavController().navigate(R.id.detail_fragment, bundle, null, extras)
        })

    }


    private fun subscribeObservers(binding: FragmentRentBinding){
        viewModel.rentPropertyPageData.observe(viewLifecycleOwner, { pagingData ->
            //Log.d("DebugApp", "Finish property")
            lifecycleScope.launch {
                pagingAdapterRent.submitData(pagingData)
            }
        })

        viewModel.rentPropertyFilter.observe(viewLifecycleOwner, {filter ->
            //Log.d("DebugApp", "Rent Filter: ${filter}")
            //binding.isLoading = true
            viewModel.setStateEvent(MainStateEvent.GetRentPropertyList(filter))
        })

        viewModel.rentLoading.observe(viewLifecycleOwner, {loading ->
            //Log.d("DebugApp", "Loading $loading")
            binding.isLoading = loading
        })

        connectionLiveData.observe(viewLifecycleOwner, {isNetworkAvailable ->
            viewModel.setStateEvent(MainStateEvent.UpdateNetworkAvailable(isNetworkAvailable))
        })
    }

}