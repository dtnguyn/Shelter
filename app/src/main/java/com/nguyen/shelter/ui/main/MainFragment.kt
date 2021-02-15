package com.nguyen.shelter.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.nguyen.shelter.databinding.FragmentMainBinding
import com.nguyen.shelter.ui.main.MainActivity.Companion.CURRENT_TAB
import com.nguyen.shelter.ui.main.adapters.MainPagerAdapter
import com.nguyen.shelter.ui.main.fragments.DialogAuthentication
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.view.*

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()


    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel.setStateEvent(MainStateEvent.CheckAuthentication)

        binding.isLogged = false
        binding.collapseArea.setOnClickListener {
            val newDialog = DialogAuthentication(viewModel, requireActivity())
            newDialog.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            newDialog.show(requireActivity().supportFragmentManager, "Authentication")
        }

        binding.loggedCollapseArea.container.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.loggedCollapseArea.userAvatarCardView to "avatar_main"
            )
            val action = MainFragmentDirections.actionMainFragmentToUserFragment()
            NavHostFragment.findNavController(this@MainFragment).navigate(action, extras)

        }

        subscribeObserver()

        return binding.root
    }



    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        pagerAdapter = MainPagerAdapter(this)
        viewPager = view.main_pager
        viewPager.adapter = pagerAdapter
        val tabLayout = view.tab_layout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Rent"
                1 -> tab.text = "Sale"
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> {
                        CURRENT_TAB = "rent"
                        (activity as MainActivity?)?.changeBackgroundToRent()
                    }
                    1 -> {
                        CURRENT_TAB = "sale"
                        (activity as MainActivity?)?.changeBackgroundToSale()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun subscribeObserver(){
        viewModel.currentUser.observe(viewLifecycleOwner, {
            println("debug: Updating UI")
            if(it == null){
                binding.isLogged = false
                (activity as MainActivity?)?.hideUserMenuItem()
            } else {
                binding.isLogged = true
                binding.loggedCollapseArea.url = it.photoUrl.toString()
                binding.loggedCollapseArea.username = it.displayName
                (activity as MainActivity?)?.showUserMenuItem()
            }
        })
    }

    override fun onResume() {
        (activity as MainActivity?)?.showActionBar()
        super.onResume()
    }

    override fun onStop() {
        (activity as MainActivity?)?.hideActionBar()
        super.onStop()
    }


}