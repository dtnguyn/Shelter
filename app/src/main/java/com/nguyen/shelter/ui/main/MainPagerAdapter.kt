package com.nguyen.shelter.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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