package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.ui.main.adapters.PhotoDetailAdapter
import kotlinx.android.synthetic.main.fragment_photo_view_pager.view.*


class PhotoViewPagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo_view_pager, container, false)

        val photos: List<Photo>? = arguments?.getParcelableArrayList<Photo>("photos")?.toList()
        val position: Int? = arguments?.getInt("position")
        photos?.let {
            val viewPager = view.photo_detail_view_pager
            viewPager.adapter = PhotoDetailAdapter(it, requireContext())
            viewPager.setCurrentItem(position  ?: viewPager.currentItem, false)
        } ?: println("debug: photo null")


        return view
    }


}