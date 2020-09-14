package com.nguyen.shelter.ui.main.adapters

import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nguyen.shelter.ui.main.fragments.RentFragment
import com.nguyen.shelter.ui.main.fragments.SaleFragment

@ExperimentalPagingApi
class MainPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    private val rentFragment = RentFragment()
    private val saleFragment = SaleFragment()



    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> rentFragment
            1 -> saleFragment
            else -> rentFragment
        }
    }

}