package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentRentBinding
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.repo.MainRepository.Companion.RENT
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.MainFragmentDirections
import com.nguyen.shelter.ui.main.adapters.RentPropertyAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import com.skydoves.transformationlayout.addTransformation
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_rent.*
import kotlinx.android.synthetic.main.property_item.*
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

    @Inject
    lateinit var cacheMapper: PropertyCacheMapper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onTransformationStartContainer()
    }


    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRentBinding.inflate(inflater, container, false)
        createAdapter()

        val rentRecyclerView = binding.rentRecyclerview

        rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rentRecyclerView.adapter = pagingAdapterRent


        subscribeObservers()

        viewModel.setStateEvent(MainStateEvent.GetPropertyFilter(RENT))


        return binding.root
    }

    @ExperimentalPagingApi
    private fun createAdapter(){

        pagingAdapterRent = RentPropertyAdapter(cacheMapper,
        detailOnClick = {bundle, transformationLayout ->
            //val bundle = bundleOf("id" to id, "photo" to photos)
//            findNavController().navigate(R.id.detail_fragment, bundle)
//            val extras = FragmentNavigatorExtras(
//                preview to "preview"
//            )
//            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(id, photos[0].url ?: "")
//            NavHostFragment.findNavController(this@RentFragment).navigate(action, extras)

            val extras = FragmentNavigatorExtras(transformationLayout to bundle.getString("id")!!)

//            val bundle = bundleOf("id" to id, "photo" to photos[0].url)
//            findNavController().navigate(R.id.detail_fragment, bundle, null, extras)


//            val fragment = DetailFragment()
//            fragment.arguments = bundle

            //val extras = FragmentNavigatorExtras(transformationLayout to "preview")
            findNavController().navigate(R.id.detail_fragment, bundle, null, extras)

        })

    }


    private fun subscribeObservers(){
        viewModel.rentPropertyPageData.observe(viewLifecycleOwner, { pagingData ->
            lifecycleScope.launch {
                pagingAdapterRent.submitData(pagingData)
            }
        })

        viewModel.rentPropertyFilter.observe(viewLifecycleOwner, {filter ->
            viewModel.setStateEvent(MainStateEvent.GetRentPropertyList(filter))
        })
    }

}