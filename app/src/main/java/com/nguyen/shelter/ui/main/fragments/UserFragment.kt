package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentUserBinding
import com.nguyen.shelter.ui.main.adapters.UserPagerAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var pagerAdapter: UserPagerAdapter
    private lateinit var viewPager: ViewPager2

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


        binding.collapseArea.logoutButton.setOnClickListener {
            viewModel.setStateEvent(MainStateEvent.Logout)
            findNavController().popBackStack()
        }

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.CheckAuthentication)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter = UserPagerAdapter(this)
        viewPager = binding.userViewPager
        viewPager.adapter = pagerAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.saved)
                1 -> tab.text = getString(R.string.blogs)
            }
        }.attach()

    }


    private fun subscribeObservers(){
        viewModel.currentUser.observe(viewLifecycleOwner, {
            binding.collapseArea.user = it
        })
    }


    fun collapseOrExpandAppBar(expand: Boolean){
        binding.appBar.setExpanded(expand)
    }

}