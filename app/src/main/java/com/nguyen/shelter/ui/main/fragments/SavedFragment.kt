package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentSavedBinding
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.ui.main.adapters.SavedAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@ExperimentalPagingApi
@AndroidEntryPoint
class SavedFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSavedBinding.inflate(inflater, container, false)

        subscribeObservers()

        viewModel.setStateEvent(MainStateEvent.GetSavedProperties)

        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.savedProperties.observe(viewLifecycleOwner, {map ->
            val savedList = ArrayList<Property>()
            for ((key, value) in map) {
                value?.let{
                    savedList.add(it)
                }
            }
            val recyclerView = binding.savedRecyclerview

            val adapter = SavedAdapter(savedList){bundle, transformationLayout ->
                val extras = FragmentNavigatorExtras(transformationLayout to bundle.getParcelable<Property>("prop")!!.id)
//                findNavController().navigate(R.id.detail_fragment, bundle, null, extras)
                findNavController().navigate(R.id.detail_fragment, bundle)

            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

        })

    }


}