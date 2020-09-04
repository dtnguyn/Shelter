package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.transition.TransitionInflater
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentUserBinding
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition =  TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        binding = FragmentUserBinding.inflate(inflater, container, false)


        binding.logoutButton.setOnClickListener {
            viewModel.setStateEvent(MainStateEvent.Logout)
            findNavController().popBackStack()
        }

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.CheckAuthentication)

        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            binding.user = it
        })
    }


}