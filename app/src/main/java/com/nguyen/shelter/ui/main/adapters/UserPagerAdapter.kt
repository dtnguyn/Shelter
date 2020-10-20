package com.nguyen.shelter.ui.main.adapters

import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nguyen.shelter.ui.main.fragments.RentFragment
import com.nguyen.shelter.ui.main.fragments.SaleFragment
import com.nguyen.shelter.ui.main.fragments.SavedFragment
import com.nguyen.shelter.ui.main.fragments.UserBlogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class UserPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    private val savedFragment = SavedFragment()
    private val userBlogFragment = UserBlogFragment()



    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> savedFragment
            1 -> userBlogFragment
            else -> savedFragment
        }
    }

}